<template>
  <div class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
       v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="absolute top-1 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <i class="pi pi-times-circle text-xl"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.history }}</div>
      <div class="flex flex-row md:min-w-[400px] rounded-xl bg-background-2 px-4 py-2 text-sm md:text-base">
        <div class="w-[35%]">{{ local.plan }}</div>
        <div class="w-[35%] text-center">{{ local.date }}</div>
        <div class="w-[30%] text-center">{{ local.payStatus }}</div>
      </div>
      <div class="h-full w-full flex justify-center pt-12" v-if="!isLoading && subscriptionRenewLog.length === 0">
        <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
          <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-3 px-2">{{ local.noRecords }}</div>
        </div>
      </div>
      <div class="h-full w-full flex justify-center items-center pt-16" v-if="isLoading">
        <loader/>
      </div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar" v-if="!isLoading && subscriptionRenewLog.length > 0">
        <div class="flex flex-row md:min-w-[400px] rounded-xl bg-background-2 px-4 py-2 mt-3 text-xs md:text-sm items-center" v-for="(renew , index) in subscriptionRenewLog">
          <div class="w-[35%] flex">
            <div>{{ renew.totalFlow }} GB</div>
            -
            <div>{{ renew.periodLength }} {{ local.days }}</div>
          </div>
          <div class="w-[35%] text-center">{{ renew.createDate.substring(0,10) }}</div>
          <div class="w-[30%] text-center">
            <div
                class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-fit text-success relative mx-auto"
                :class="{'cursor-pointer' : userType === 'Admin'}"
                v-if="renew.markAsPaid" @mouseenter="markAsNotPaidTag = index"
                @mouseleave="markAsNotPaidTag = undefined" @click="changePayStatus(false , index)">{{ local.paid }}
              <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
                   :class="{'-left-0' : !isRtl , '-left-4' : isRtl}" v-if="userType === 'Admin' && markAsNotPaidTag === index">{{ local.markAsNotPaid }}
              </div>
            </div>
            <div
                class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-fit text-error relative mx-auto"
                :class="{'cursor-pointer' : userType === 'Admin'}"
                v-if="!renew.markAsPaid" @mouseenter="markAsPaidTag = index"
                @mouseleave="markAsPaidTag = undefined" @click="changePayStatus(true , index)">{{ local.notPaid }}
              <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
                   :class="{'-left-0' : !isRtl , '-left-4' : isRtl}" v-if="userType === 'Admin' && markAsPaidTag === index">{{ local.markAsPaid }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import {displayHelper} from "../helpers/displayHelper.js";
import Loader from "./loader.vue";

let local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

let isLoading = ref(true)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})
const props = defineProps(['showDialog' , 'subscription' , 'userType'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let markAsNotPaidTag = ref(undefined)
let markAsPaidTag = ref(undefined)

const isRtl = computed(()=>{
  return useLocalization().getDirection === 'rtl'
})

const emits = defineEmits(['closeDialog'])

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}

let subscriptionRenewLog = ref([])

watch(() => props.showDialog , () => {
  if(props.showDialog){
    subscriptionRenewLog.value = []
    getRenewLog()
  }
})

const getRenewLog = () => {
  isLoading.value = true
    axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all-renew-list?subscriptionId=${props.subscription.id}`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      subscriptionRenewLog.value = response.data.content
      isLoading.value = false
    }).catch(() => {
      errorMessage.value = local.value.errorGettingHistory
      isLoading.value = false
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    })
}

const changePayStatus = (status , index) => {
  if(props.userType === 'Admin') {
    axios.put(`${useDataStore().getServerAddress}/subscriptions/change-pay-status-for-subscription-renew-log?newPayStatus=${status}&renewId=${subscriptionRenewLog.value[index].id}`,
        {},
        {
          headers : {
            Authorization : useDataStore().getToken
          }
        }
    ).then(() => {
      markAsNotPaidTag.value = undefined
      markAsPaidTag.value = undefined
      subscriptionRenewLog.value[index].markAsPaid = status
    }).catch(() => {
      errorMessage.value = local.value.errorOccurredWhileChangingStatus
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      } , 1000)
    })
  }
}

</script>