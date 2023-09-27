<template>
  <div class="absolute min-h-screen min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
       v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="absolute top-1 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <x-circle-icon class="w-5 h-5"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="absolute top-1 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <check-circle-icon class="w-5 h-5"/>
      <div>{{ local.passwordChangedSuccessfully }}</div>
    </div>
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emptyFields();emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.password }}</div>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : oldPassword}">{{local.oldPassword}}</label>
      <input type="text" :placeholder="local.oldPassword" v-model="oldPassword" v-if="isSelf"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : newPassword}">{{local.newPassword}}</label>
      <input type="text" :placeholder="local.newPassword" v-model="newPassword"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex justify-end">
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
import {computed, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon, XCircleIcon, CheckCircleIcon} from "@heroicons/vue/24/solid/index.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import {useRoute, useRouter} from "vue-router";
import router from "../router/index.js";

let local = computed(() => {
  return useLocalization().getLocal
})


let oldPassword = ref('')
let newPassword = ref('')
const props = defineProps(['showDialog' , 'userId' , 'isSelf'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)

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
  console.log(router)
  if (props.isSelf) {
    if(newPassword.value && oldPassword.value){
      axios.put(`${useDataStore().getServerAddress}/users/change-password`,
          {
            oldPassword : oldPassword.value,
            newPassword : newPassword.value
          },
          {
            headers: {
              Authorization: useDataStore().getToken
            }
          }
      ).then(() => {
        showSuccessMessage.value = true
        setTimeout(() => {
          showSuccessMessage = false
          emptyFields()
          emits('closeDialog')
        }, 1000)
      }).catch(() => {
        errorMessage.value = local.value.errorChangingPassword
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      })
    }
    else {
      errorMessage.value = local.value.errorFieldsOfPassword
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    }
  }
  else {
    if(newPassword.value){
      axios.put(`${useDataStore().getServerAddress}/users/change-password`,
          {
            userId : props.userId,
            newPassword : newPassword.value
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
        }, 1000)
      }).catch(() => {
        errorMessage.value = local.value.errorChangingPassword
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      })
    }
    else {
      errorMessage.value = local.value.errorFieldsOfPassword
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    }
  }
}

</script>