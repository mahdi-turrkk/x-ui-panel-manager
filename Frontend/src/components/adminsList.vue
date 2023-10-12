<template>
  <div>
    <div class="flex w-full items-center space-x-2 my-2 bg-background-3 rounded-xl text-info-3 font-bold px-4 py-4">
      <div class="w-[20%] no-scrollbar text-sm md:text-base">{{ local.id }}</div>
      <div class="w-[40%] no-scrollbar text-center text-sm md:text-base">{{ local.username }}</div>
      <div class="w-[20%] no-scrollbar text-center text-sm md:text-base">{{ local.status }}</div>
      <div class="w-[20%] text-center text-sm md:text-base">{{ local.actions }}</div>
      <i class="pi pi-chevron-down text-base md:text-lg mx-1 text-background-3"/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-16" v-if="isLoading">
      <loader/>
    </div>
    <div class="h-full w-full flex justify-center items-center pt-12" v-if="!isLoading && admins.length === 0">
      <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
        <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-1 px-2">{{ local.noRecords }}</div>
      </div>
    </div>
    <admins-list-item @open-edit-admin-dialog="openEditAdminDialog" @open-link-dialog="openLinkDialog"
                         v-for="admin in admins" :onboarding="onboarding" @set-onboarding="setOnboarding" @open-change-password-dialog="openChangePasswordDialog" @open-delete-confirmation-dialog="openDeleteConfirmationDialog"
                         :admin="admin" @change-admin-status="(payload) => {admin.enabled = payload}" v-if="!isLoading && admins.length > 0" @open-delete-sub-confirmation-dialog="(payload) => {emits('openDeleteSubConfirmationDialog' , payload)}"/>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import Loader from "./loader.vue";
import AdminsListItem from "./adminsListItem.vue";

const props = defineProps(['admins', 'isLoading'])
const emits = defineEmits(['openEditAdminDialog', 'openLinkDialog' ,'openChangePasswordDialog' , 'openDeleteConfirmationDialog' ,'openDeleteSubConfirmationDialog'])

const setOnboarding = (index) => {
  if (index == onboarding.value) {
    onboarding.value = undefined
  } else
    onboarding.value = index
}

let onboarding = ref(undefined)

const openEditAdminDialog = (payload) => {
  emits('openEditAdminDialog', payload)
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
</script>

<style>
</style>