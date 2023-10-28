<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="fixed top-4 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <i class="pi pi-times-circle text-xl"/>
      <div>{{ local.errorOccurredWhileDeleting }}</div>
    </div>
    <div class="fixed top-4 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <i class="pi pi-check-circle text-xl"/>
      <div>{{ local.deletedSuccessfully }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-[350px] rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.deleteConfirmation }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <div>{{ local.deleteConfirmationMessage }}</div>
      </div>
      <div class="flex justify-end mt-4">
        <button
            @click="emits('closeDialog')"
            class="rounded-xl py-1 px-4 text-primary-3 border border-primary-3 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button
            class="rounded-xl bg-error py-1 px-4 text-white brightness-75 hover:brightness-125 transition-all duration-150"
            @click="confirmDelete">
          {{ local.delete }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import {displayHelper} from "../helpers/displayHelper.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
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


