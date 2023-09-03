<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[20%] md:w-[10%]">{{ local.id }}</div>
      <div class="w-[30%] md:w-[15%] text-center">{{ local.title }}</div>
      <div class="hidden md:inline-block md:w-[22%] text-center">{{ local.startEndDate }}</div>
      <div class="hidden md:inline-block md:w-[23%] text-center" style="direction: ltr">{{ local.usage }}</div>
      <div class="w-[30%] md:w-[15%] text-center">{{ local.subscription }} {{ local.status }}</div>
      <div class="w-[20%] md:w-[15%] flex justify-center text-center">{{ local.actions }}</div>
    </div>
    <detailed-subscription-list-item @open-renew-subscription-dialog="openRenewSubscriptionDialog" v-for="subscription in subscriptions" :subscription="subscription"/>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {ChevronDownIcon} from "@heroicons/vue/24/solid/index.js";
import DetailedSubscriptionListItem from "./detailedSubscriptionListItem.vue";

const props = defineProps(['subscriptions'])
const emits = defineEmits(['openRenewSubscriptionDialog'])

const setOnboarding = (index) =>{
  if(index == onboarding.value){
    onboarding.value = undefined
  }
  else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openRenewSubscriptionDialog = (payload) => {
  emits('openRenewSubscriptionDialog' , payload)
}

let local = computed(()=>{
  return useLocalization().getLocal
})
</script>

<style>
</style>