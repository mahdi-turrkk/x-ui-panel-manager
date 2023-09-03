<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[20%] no-scrollbar">{{ local.id }}</div>
      <div class="w-[40%] no-scrollbar text-center">{{ local.username }}</div>
      <div class="w-[20%] no-scrollbar text-center">{{ local.status }}</div>
      <div class="w-[20%] flex justify-center no-scrollbar">{{ local.actions }}</div>
      <chevron-down-icon class="h-5 w-5 text-background-3"/>
    </div>
    <customers-list-item @open-edit-customer-dialog="openEditCustomerDialog" v-for="customer in customers" :onboarding="onboarding"  @set-onboarding="setOnboarding" :customer="customer.customer" :subscriptions="customer.subscriptions"/>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {ChevronDownIcon} from "@heroicons/vue/24/solid/index.js";
import CustomersListItem from "./customersListItem.vue";
import {PencilSquareIcon} from "@heroicons/vue/24/outline/index.js";

const props = defineProps(['customers'])
const emits = defineEmits(['openEditCustomerDialog'])

const setOnboarding = (index) =>{
  if(index == onboarding.value){
    onboarding.value = undefined
  }
  else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openEditCustomerDialog = (payload) => {
  emits('openEditCustomerDialog' , payload)
}

let local = computed(()=>{
  return useLocalization().getLocal
})
</script>

<style>
</style>