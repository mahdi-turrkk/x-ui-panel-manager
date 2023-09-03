<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[25%] lg:w-[10%] no-scrollbar">{{ local.id }}</div>
      <div class="hidden lg:inline-block lg:w-[30%] no-scrollbar">{{ local.serverUrl }}</div>
      <div class="hidden lg:inline-block lg:w-[10%] no-scrollbar">{{ local.username }}</div>
      <div class="hidden lg:inline-block lg:w-[30%] overflow-y-scroll no-scrollbar">{{ local.password }}</div>
      <div class="w-[25%] lg:w-[10%] no-scrollbar">{{ local.generatable }}</div>
      <div class="w-[50%] lg:w-[10%] flex justify-center no-scrollbar">{{ local.actions }}</div>
      <chevron-down-icon class="h-5 w-5 text-background-3"/>
    </div>
    <servers-list-item @open-edit-server-dialog="openEditServerDialog" v-for="server in servers" :onboarding="onboarding"  @set-onboarding="setOnboarding" :server="server.server" :inbounds="server.inbounds"/>
  </div>
</template>

<script setup>
import ServersListItem from "./serversListItem.vue";
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {ChevronDownIcon} from "@heroicons/vue/24/solid/index.js";

const props = defineProps(['servers'])
const emits = defineEmits(['openEditServerDialog'])

const setOnboarding = (index) =>{
  if(index == onboarding.value){
    onboarding.value = undefined
  }
  else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openEditServerDialog = (payload) => {
  emits('openEditServerDialog' , payload)
}

let local = computed(()=>{
  return useLocalization().getLocal
})
</script>

<style>
</style>