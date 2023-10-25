<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[25%] lg:w-[10%] no-scrollbar text-sm md:text-base">{{ local.id }}</div>
      <div class="hidden lg:inline-block lg:w-[30%] no-scrollbar text-sm md:text-base">{{ local.serverUrl }}</div>
      <div class="hidden lg:inline-block lg:w-[10%] no-scrollbar text-sm md:text-base">{{ local.username }}</div>
      <div class="hidden lg:inline-block lg:w-[30%] overflow-y-scroll no-scrollbar text-sm md:text-base">{{ local.password }}</div>
      <div class="w-[25%] lg:w-[10%] no-scrollbar text-sm md:text-base text-center">{{ local.status }}</div>
      <div class="w-[50%] lg:w-[10%] flex justify-center no-scrollbar text-sm md:text-base text-center">{{ local.actions }}</div>
      <i class="pi pi-chevron-down text-base md:text-lg mx-1 text-background-3"/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-16" v-if="isLoading">
      <loader/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-12" v-if="!isLoading && servers.length === 0">
      <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
        <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-1 px-2">{{ local.noRecords }}</div>
      </div>
    </div>
    <servers-list-item @open-edit-server-dialog="openEditServerDialog" v-for="server in servers"
                       :onboarding="onboarding" @set-onboarding="setOnboarding" :server="server" @open-delete-confirmation-dialog="openDeleteConfirmationDialog"
                       @change-server-status="(payload) => {server.status = payload}" v-if="!isLoading && servers.length > 0"/>
  </div>
</template>

<script setup>
import ServersListItem from "./serversListItem.vue";
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import Loader from "./loader.vue";

const props = defineProps(['servers', 'isLoading'])
const emits = defineEmits(['openEditServerDialog' , 'openDeleteConfirmationDialog'])

const setOnboarding = (index) => {
  if (index == onboarding.value) {
    onboarding.value = undefined
  } else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openEditServerDialog = (payload) => {
  emits('openEditServerDialog', payload)
}

let local = computed(() => {
  return useLocalization().getLocal
})

const openDeleteConfirmationDialog = (payload) => {
  emits('openDeleteConfirmationDialog' , payload)
}
</script>

<style>
</style>