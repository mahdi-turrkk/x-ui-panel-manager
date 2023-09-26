<template>
  <div class="w-full rounded-xl text-xs overflow-hidden my-2">
    <div>
      <div
          class="bg-background-3 flex rounded-t-xl justify-between items-center px-4 py-4 z-20 relative cursor-pointer text-info-3"
          :class="{'rounded-b-xl' : admin.id != onboarding}">
        <div class="flex w-full items-center space-x-2">
          <div class="w-[20%] no-scrollbar" @click="emits('setOnboarding' , admin.id)">{{ admin.id }}</div>
          <div class="w-[40%] no-scrollbar text-center" @click="emits('setOnboarding' , admin.id)">
            {{ admin.username }}
          </div>
          <div class="w-[20%] no-scrollbar flex justify-center">
            <div
                class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 text-success relative w-fit px-4"
                v-if="admin.enabled" @mouseenter="showDeactivateTag = true"
                @mouseleave="showDeactivateTag = false" @click="changeStatus(false)">{{ local.active }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-error"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateTag">{{ local.deactivate }}
                {{ local.admin }}
              </div>
            </div>
            <div
                class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 text-error relative w-fit px-4"
                v-if="!admin.enabled" @mouseenter="showActivateTag = true" @mouseleave="showActivateTag = false" @click="changeStatus(true)">
              {{ local.inactive }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-success"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateTag">{{ local.activate }}
                {{ local.admin }}
              </div>
            </div>
          </div>
          <div class="w-[20%] flex justify-center no-scrollbar">
            <div class="relative">
              <pencil-square-icon class="w-6 h-6 text-warning mx-1" @mouseenter="showEditTag = true"
                                  @mouseleave="showEditTag = false"
                                  @click="emits('openEditAdminDialog' , admin)"/>
              <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showEditTag">{{ local.edit }}
                {{ local.admin }}
              </div>
            </div>
          </div>
        </div>
        <chevron-down-icon class="h-5 w-5 transition-all duration-300" @click="emits('setOnboarding' , admin.id)"
                           :class="{'rotate-180' : onboarding === admin.id}"/>
      </div>
      <div class="bg-background-2 px-8 md:px-12 py-4 transition-all duration-700 z-10 relative rounded-b-xl text-info-3"
           ref="expansionText"
           :style="{'margin-top' : marginTop}">
        <div class="text-sm font-bold">{{ local.subscriptions }} :</div>
        <div
            class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md mb-2 mt-4 font-bold text-sm"
            :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
          <div class="w-0 hidden md:inline-block md:w-[10%] text-xs md:text-sm">{{ local.id }}</div>
          <div class="w-[30%] text-center text-xs md:text-sm">{{ local.startEndDate }}</div>
          <div class="w-[30%] text-center text-xs md:text-sm">{{ local.usage }}</div>
          <div class="w-[30%] md:w-[20%] text-center text-xs md:text-sm">{{ local.status }}</div>
          <div class="w-[10%] text-center text-xs md:text-sm">{{ local.url }}</div>
        </div>
        <div class="h-full w-full flex justify-center items-center pt-12" v-if="subscriptions.length === 0">
          <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
            <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-2 px-2">{{ local.noRecords }}</div>
          </div>
        </div>
        <subscription-list-item v-for="subscription in subscriptions" :subscription="subscription" v-if="subscriptions.length > 0"
                                @open-link-dialog="openLinkDialog"
                                @change-subscription-status="(payload) => {subscription.status = payload}"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ChevronDownIcon, ArrowPathIcon, PencilSquareIcon, PlusIcon} from "@heroicons/vue/24/outline";
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {useDataStore} from "../store/dataStore.js";
import SubscriptionListItem from "./subscriptionListItem.vue";
import {QrCodeIcon} from "@heroicons/vue/24/outline/index.js";
import axios from "axios";

let props = defineProps(['onboarding', 'admin'])
const emits = defineEmits(['setOnboarding', 'openEditAdminDialog', 'openLinkDialog', 'changeAdminStatus'])

const expansionText = ref(null)

let marginTop = computed(() => {
  if (expansionText.value != null) {
    if (props.onboarding !== props.admin.id)
      return `-${expansionText.value.offsetHeight}px`
    else return 0
  }
})

let local = computed(() => {
  return useLocalization().getLocal
})

let showEditTag = ref(false)
let showActivateTag = ref(false)
let showDeactivateTag = ref(false)

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

const openLinkDialog = (payload) => {
  emits('openLinkDialog', payload)
}

let subscriptions = ref([])

onMounted(() => {
  axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all?userId=${props.admin.id}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    subscriptions.value = response.data.content
    emits('setOnboarding', props.admin.id)
    setTimeout(() => {
      emits('setOnboarding', undefined)
    }, 1)
  }).catch((error) => console.log(error))
})

const changeStatus = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/users/change-status?id=${props.admin.id}&newStatus=${payload}` ,
      {},
      {
        headers : {
          authorization : useDataStore().getToken
        }
      }
  ).then((response) => {
    emits('changeAdminStatus' , payload)
  }).catch((error) => {console.log(error)})
}
</script>


<style>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>