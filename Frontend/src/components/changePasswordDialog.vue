<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="fixed top-4 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <i class="pi pi-times-circle text-xl"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="fixed top-4 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <i class="pi pi-check-circle text-xl"/>
      <div>{{ local.passwordChangedSuccessfully }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 md:bottom-auto max-h-500 md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emptyFields();emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.changePassword }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar pt-2">
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : oldPassword}">{{ local.oldPassword }}</label>
        <input type="text" :placeholder="local.oldPassword" v-model="oldPassword" v-if="isSelf"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : newPassword}">{{ local.newPassword }}</label>
        <input type="text" :placeholder="local.newPassword" v-model="newPassword"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      </div>
      <div class="flex justify-end mt-2">
        <button
            @click="emptyFields();emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="changePassword">
          {{ local.save }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import 'primeicons/primeicons.css';
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

let oldPassword = ref('')
let newPassword = ref('')
const props = defineProps(['showDialog', 'userId', 'isSelf'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)
let isRequestInProgress = ref(false)

const emits = defineEmits(['closeDialog'])

const emptyFields = () => {
  oldPassword.value = ''
  newPassword.value = ''
}

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emptyFields()
    emits('closeDialog')
  }
}

const changePassword = () => {
  if (isRequestInProgress.value){
    errorMessage = local.value.requestInProgress
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 1000)
  }
  else {
    if (props.isSelf) {
      if (newPassword.value && oldPassword.value) {
        isRequestInProgress.value = true
        axios.put(`${useDataStore().getServerAddress}/users/change-password`,
            {
              oldPassword: oldPassword.value,
              newPassword: newPassword.value
            },
            {
              headers: {
                Authorization: useDataStore().getToken
              }
            }
        ).then(() => {
          showSuccessMessage.value = true
          setTimeout(() => {
            showSuccessMessage.value = false
            emptyFields()
            emits('closeDialog')
            isRequestInProgress.value = false
          }, 1000)
        }).catch(() => {
          errorMessage.value = local.value.errorChangingPassword
          showErrorMessage.value = true
          setTimeout(() => {
            showErrorMessage.value = false
            isRequestInProgress.value = false
          }, 2000)
        })
      } else {
        errorMessage.value = local.value.errorFieldsOfPassword
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      }
    } else {
      if (newPassword.value) {
        isRequestInProgress.value = true
        axios.put(`${useDataStore().getServerAddress}/users/change-password`,
            {
              userId: props.userId,
              newPassword: newPassword.value
            },
            {
              headers: {
                Authorization: useDataStore().getToken
              }
            }
        ).then(() => {
          showSuccessMessage.value = true
          setTimeout(() => {
            showSuccessMessage.value = false
            emptyFields()
            emits('closeDialog')
            isRequestInProgress.value = false
          }, 1000)
        }).catch(() => {
          errorMessage.value = local.value.errorChangingPassword
          showErrorMessage.value = true
          setTimeout(() => {
            showErrorMessage.value = false
            isRequestInProgress.value = false
          }, 2000)
        })
      } else {
        errorMessage.value = local.value.errorFieldsOfPassword
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      }
    }
  }
}
</script>


