<template>
  <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md my-2 text-info-3"
       :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
    <div class="w-0 hidden md:inline-block md:w-[10%]">{{ subscription.id }}</div>
    <div class="hidden md:flex md:w-[25%] justify-center">
      <div>
        {{ subscription.totalFlow ? subscription.totalFlow + 'GB' : '0.00 GB' }}
      </div>
      -
      <div>
        {{ subscription.periodLength + local.days }}
      </div>
    </div>
    <div class="flex justify-center w-[30%] md:w-[20%] flex-col md:flex-row text-center" style="direction: ltr">
      <div class="text-center">
        {{ subscription.totalUsed ? (subscription.totalFlow - subscription.totalUsed).toFixed(2) : subscription.totalFlow.toFixed(2) }} GB
      </div>
      <div class="hidden md:block">&nbsp;/&nbsp;</div>
      <div class="block">
        {{ subscription.expireDate ? subscription.expireDate.substring(0,10) : '' }}
      </div>
    </div>
    <div class="w-[30%] md:w-[20%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-fit text-success relative mx-auto cursor-pointer"
          v-if="subscription.markAsPaid" @mouseenter="showMarkAsNotPaidTag = true"
          @mouseleave="showMarkAsNotPaidTag = false" @click="changePayStatus(false)">{{ local.paid }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showMarkAsNotPaidTag">{{ local.markAsNotPaid }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-fit text-error relative mx-auto cursor-pointer"
          v-if="!subscription.markAsPaid" @mouseenter="showMarkAsPaidTag = true"
          @mouseleave="showMarkAsPaidTag = false" @click="changePayStatus(true)">{{ local.notPaid }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showMarkAsPaidTag">{{ local.markAsPaid }}
        </div>
      </div>
    </div>
    <div class="w-[30%] md:w-[15%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
          v-if="subscription.status" @mouseenter="showDeactivateSubscriptionTag = true"
          @mouseleave="showDeactivateSubscriptionTag = false" @click="changeStatus(false)">{{ local.active }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateSubscriptionTag">{{
            local.deactivate
          }}
          {{ local.subscription }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
          v-if="!subscription.status" @mouseenter="showActivateSubscriptionTag = true"
          @mouseleave="showActivateSubscriptionTag = false" @click="changeStatus(true)">{{ local.inactive }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateSubscriptionTag">{{ local.activate }}
          {{ local.subscription }}
        </div>
      </div>
    </div>
    <div class="w-[10%] flex justify-center relative" @mouseenter="showMenu = true" @mouseleave="showMenu = false"
         @click="showMenu = !showMenu">
      <div>
        <button class="flex justify-center items-center p-2">
          <i class="pi pi-cog text-lg md:text-xl mx-1 text-info-3"/>
        </button>
      </div>
      <div class="absolute flex w-fit bg-background-1 p-2 rounded-xl" v-if="showMenu">
        <div class="relative cursor-pointer">
          <i class="pi pi-history text-lg md:text-xl text-warning mx-1" @mouseenter="showRenewHistoryTag = true"
             @mouseleave="showRenewHistoryTag = false" @click="emits('openRenewHistoryDialog' , subscription)"/>
          <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
               :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showRenewHistoryTag">{{ local.show }}
            {{ local.renewHistory }}
          </div>
        </div>
        <div class="relative cursor-pointer">
          <i class="pi pi-qrcode text-lg md:text-xl mx-1 text-success" @mouseenter="showUrlTag = true"
                        @mouseleave="showUrlTag = false" @click="emits('openLinkDialog' , subscription.link)"/>
          <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
               :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showUrlTag">{{ local.show }}
            {{ local.url }}
          </div>
        </div>
        <div class="relative cursor-pointer">
          <i class="pi pi-trash text-lg md:text-xl mx-1 text-error" @mouseenter="showDeleteTag = true"
                      @mouseleave="showDeleteTag = false"
                      @click="emits('openDeleteConfirmationDialog' , subscription)"/>
          <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
               :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeleteTag">{{ local.delete }}
            {{ local.subscription }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {useDataStore} from "../store/dataStore.js";
import {useLocalization} from "../store/localizationStore.js";
import {computed, ref} from "vue";
import axios from "axios";
import 'primeicons/primeicons.css';


const props = defineProps(['subscription' , 'userType'])
const emits = defineEmits(['openLinkDialog', 'changeSubscriptionStatus', 'openDeleteConfirmationDialog', 'changeSubscriptionPayStatus' , 'openRenewHistoryDialog'])

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})
let local = computed(() => {
  return useLocalization().getLocal
})

let showActivateSubscriptionTag = ref(false)
let showDeactivateSubscriptionTag = ref(false)
let showMarkAsPaidTag = ref(false)
let showMarkAsNotPaidTag = ref(false)
let showUrlTag = ref(false)
let showRenewHistoryTag = ref(false)
let isPayRequestInProgress = ref(false)
let isStatusRequestInProgress = ref(false)

const changeStatus = (payload) => {
  if(!isStatusRequestInProgress.value){
    isStatusRequestInProgress.value = true
    axios.put(`${useDataStore().getServerAddress}/subscriptions/change-status?id=${props.subscription.id}&newStatus=${payload}`,
        {},
        {
          headers: {
            authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      emits('changeSubscriptionStatus', payload)
      isStatusRequestInProgress.value = false
    }).catch((error) => {
      console.log(error)
      isStatusRequestInProgress.value = false
    })
  }
}

const changePayStatus = (payload) => {
  if(!isPayRequestInProgress.value) {
    isPayRequestInProgress.value = true
    axios.put(`${useDataStore().getServerAddress}/subscriptions/change-pay-status-for-subscription?id=${props.subscription.id}&newPayStatus=${payload}`,
        {},
        {
          headers: {
            authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      emits('changeSubscriptionPayStatus', payload)
      isPayRequestInProgress.value = false
    }).catch((error) => {
      console.log(error)
      isPayRequestInProgress.value = false
    })
  }
}


let showMenu = ref(false)
let showDeleteTag = ref(false)

</script>