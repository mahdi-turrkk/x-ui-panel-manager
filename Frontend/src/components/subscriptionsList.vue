<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[15%] md:w-[10%] text-sm md:text-base">{{ local.id }}</div>
      <div class="hidden md:inline-block md:w-[20%] text-center text-sm md:text-base">{{ local.title }}</div>
      <div class="text-center text-sm md:text-base" :class="{'hidden md:inline-block md:w-[25%]' : userType !== 'SuperCustomer' , 'w-[33%] md:w-[40%]' : userType === 'SuperCustomer'}">{{ local.remaining }}</div>
      <div class="w-[33%] md:w-[15%] text-center text-sm md:text-base" v-if="userType !== 'SuperCustomer'" style="direction: ltr">
        {{ local.payStatus }}
      </div>
      <div class="w-[32%] md:w-[15%] text-center text-sm md:text-base">{{ local.status }}</div>
      <div class="w-[20%] md:w-[15%] flex justify-center text-center text-sm md:text-base">{{ local.actions }}</div>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-16" v-if="isLoading">
      <loader/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-12" v-if="!isLoading && subscriptions.length === 0">
      <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
        <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-1 px-2">{{ local.noRecords }}</div>
      </div>
    </div>
    <detailed-subscription-list-item @open-link-dialog="openLinkDialog"
                                     :user-type="userType"
                                     @open-renew-subscription-dialog="openRenewSubscriptionDialog"
                                     @open-delete-confirmation-dialog="openDeleteConfirmationDialog"
                                     @open-renew-history-dialog="(payload) => {emits('openRenewHistoryDialog' , payload)}"
                                     v-for="subscription in subscriptions" :subscription="subscription" @change-subscription-pay-status="(payload) => {subscription.markAsPaid = payload}"
                                     @change-subscription-status="(payload) => {subscription.status = payload}" v-if="!isLoading && subscriptions.length > 0"/>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import DetailedSubscriptionListItem from "./detailedSubscriptionListItem.vue";
import Loader from "./loader.vue";

const props = defineProps(['subscriptions', 'isLoading' , 'userType'])
const emits = defineEmits(['openRenewSubscriptionDialog', 'openLinkDialog' , 'openDeleteConfirmationDialog' , 'openRenewHistoryDialog'])

const setOnboarding = (index) => {
  if (index == onboarding.value) {
    onboarding.value = undefined
  } else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openRenewSubscriptionDialog = (payload) => {
  emits('openRenewSubscriptionDialog', payload)
}

let local = computed(() => {
  return useLocalization().getLocal
})

const openLinkDialog = (payload) => {
  emits('openLinkDialog', payload)
}

const openDeleteConfirmationDialog = (payload) => {
  emits('openDeleteConfirmationDialog'  ,payload)
}
</script>

<style>
</style>