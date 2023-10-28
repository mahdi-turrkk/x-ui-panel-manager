<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="lookupSubscription = {};emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subLookUp }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <div class="flex relative w-full">
          <input type="text" :placeholder="local.subscriptionUrl" v-model="subLink"
                 class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
                 :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
          />
          <i class="pi pi-search text-xl text-info-2 absolute cursor-pointer hover:text-info-1" style="top : 9px"
             :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
             @click="searchSubscription"/>
        </div>
        <div class="font-bold text-center flex flex-col space-y-2 px-2 relative text-sm"
             v-if="showSubDetail">
          <div class="flex">{{ local.id }}&nbsp;:&nbsp;<div class="font-normal"
                                                            :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.id }}
          </div>
          </div>
          <div class="flex">{{ local.title }}&nbsp;:&nbsp;<div class="font-normal"
                                                               :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.title }}
          </div>
          </div>
          <div class="flex">{{ local.startDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.startDate ? lookupSubscription.startDate.substring(0, 10) : '' }}
          </div>
          </div>
          <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                    :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.expireDate ? lookupSubscription.expireDate.substring(0, 10) : '' }}
          </div>
          </div>
          <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.totalUsed ? Number(lookupSubscription.totalUsed).toFixed(2) : '0.00' }}&nbsp;&nbsp;&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ lookupSubscription.totalFlow ? lookupSubscription.totalFlow : '0.00' }}&nbsp;&nbsp;&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                                :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
            {{ lookupSubscription.status ? local.active : local.inactive }}
          </div>
          </div>
        </div>
        <div class="text-info-2 px-6 md:px-10 pb-14 text-center"
             v-if="!showSubDetail">
          <i class="pi pi-file text-[90px] mt-4 mb-8"/>
          <div class="text-xs md:text-sm">{{ local.enterSubToSearch }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import {displayHelper} from "../helpers/displayHelper.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

let isRtl = computed(() => useLocalization().getDirection === 'rtl')
let lookupSubscription = reactive({
  id: undefined,
  title: 'shahr-mobile',
  startDate: '2023/09/04',
  expireDate: '2023/10/05',
  totalUsed: '34534583905',
  totalFlow: '57434658206',
  status: true
})

let subLink = ref('')
let showSubDetail = ref(false)

const searchSubscription = () => {
  if (subLink.value) {
    axios.get(`${useDataStore().getServerAddress}/subscriptions/report?subLink=${subLink.value}`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      lookupSubscription = response.data
      showSubDetail.value = true
    }).catch((error) => console.log(error))
  }
}


const props = defineProps(['showDialog'])
const emits = defineEmits(['closeDialog'])

watch(() => props.showDialog, () => {
  lookupSubscription = {}
  subLink.value = ''
  showSubDetail.value = false
})

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    lookupSubscription = {}
    emits('closeDialog')
  }
}
</script>


