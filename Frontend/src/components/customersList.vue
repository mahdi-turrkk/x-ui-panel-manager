<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-0 hidden md:w-[10%] md:inline-block no-scrollbar text-sm md:text-base">{{ local.id }}</div>
      <div class="w-[30%] md:w-[20%] no-scrollbar text-center text-sm md:text-base">{{ local.remaining }}</div>
      <div class="w-[30%] no-scrollbar text-center text-sm md:text-base">{{ local.username }}</div>
      <div class="w-[20%] no-scrollbar text-center text-sm md:text-base">{{ local.status }}</div>
      <div class="w-[20%] text-center text-sm md:text-base">{{ local.actions }}</div>
      <i class="pi pi-chevron-down text-base md:text-lg mx-1 text-background-3"/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-16" v-if="isLoading">
      <loader/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-12" v-if="!isLoading && customers.length === 0">
      <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
        <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-1 px-2">{{ local.noRecords }}</div>
      </div>
    </div>
    <customers-list-item @open-edit-customer-dialog="openEditCustomerDialog" @open-link-dialog="openLinkDialog"
                         @open-renew-history-dialog="(payload) => {emits('openRenewHistoryDialog' , payload)}" :customer-type="customerType"
                         @open-add-payment-dialog="(payload) => {emits('openAddPaymentDialog' , payload)}"
                         v-for="customer in customers" :onboarding="onboarding" @set-onboarding="setOnboarding" @open-change-password-dialog="openChangePasswordDialog" @open-delete-confirmation-dialog="openDeleteConfirmationDialog"
                         @open-delete-confirmation-dialog-subscription="openDeleteConfirmationDialogSubscription" @open-renew-customer-dialog="(payload) => {emits('openRenewCustomerDialog' , payload)}"
                         :customer="customer" @change-customer-status="(payload) => {customer.enabled = payload}" v-if="!isLoading && customers.length > 0"/>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import 'primeicons/primeicons.css';
import CustomersListItem from "./customersListItem.vue";
import Loader from "./loader.vue";

const props = defineProps(['customers', 'isLoading' , 'customerType'])
const emits = defineEmits(['openEditCustomerDialog', 'openLinkDialog' , 'openChangePasswordDialog' , 'openDeleteConfirmationDialog' , 'openDeleteConfirmationDialogSubscription' , 'openRenewHistoryDialog' , 'openRenewCustomerDialog' , 'openAddPaymentDialog'])

const setOnboarding = (index) => {
  if (index == onboarding.value) {
    onboarding.value = undefined
  } else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openEditCustomerDialog = (payload) => {
  emits('openEditCustomerDialog', payload)
}

let local = computed(() => {
  return useLocalization().getLocal
})
const openLinkDialog = (payload) => {
  emits('openLinkDialog', payload)
}

const openChangePasswordDialog = (payload) => {
  emits('openChangePasswordDialog' , payload)
}

const openDeleteConfirmationDialog = (payload) => {
  emits('openDeleteConfirmationDialog' , payload)
}
const openDeleteConfirmationDialogSubscription = (payload) => {
  emits('openDeleteConfirmationDialogSubscription' , payload)
}
</script>

<style>
</style>