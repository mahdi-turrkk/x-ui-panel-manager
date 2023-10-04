<template>
  <div
      class="absolute min-h-screen min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="absolute top-1 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <x-circle-icon class="w-5 h-5"/>
      <div>{{ local.errorOccurredWhileDeleting }}</div>
    </div>
    <div class="absolute top-1 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <check-circle-icon class="w-5 h-5"/>
      <div>{{ local.deletedSuccessfully }}</div>
    </div>
    <div class="bg-background-3 text-info-3 px-12 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.deleteConfirmation }}</div>
      <div>{{ local.deleteConfirmationMessage }}</div>
      <div class="flex justify-end mt-6">
        <button
            @click="emits('closeDialog')"
            class="rounded-xl py-1 px-4 text-primary-3 border border-primary-3 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-error py-1 px-4 text-white brightness-75 hover:brightness-125 transition-all duration-150"
                @click="confirmDelete">
          {{ local.delete }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon, XCircleIcon, CheckCircleIcon} from "@heroicons/vue/24/solid/index.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";

let local = computed(() => {
  return useLocalization().getLocal
})

const props = defineProps(['showDialog', 'data', 'title'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)

const emits = defineEmits(['closeDialog', 'deleteComplete'])

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}

const confirmDelete = () => {
  console.log(props.data.value)
  axios.delete(`${useDataStore().getServerAddress}/${props.title}/delete?id=${props.data.id}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then(() => {
    showSuccessMessage.value = true
    setTimeout(() => {
      showSuccessMessage.value = false
      emits('closeDialog')
    }, 1000)
    emits('deleteComplete')
  }).catch(() => {
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  })
}

</script>