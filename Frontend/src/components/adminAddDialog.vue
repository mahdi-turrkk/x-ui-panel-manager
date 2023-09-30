<template>
  <div class="absolute min-h-screen min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
       v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="absolute top-1 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <x-circle-icon class="w-5 h-5"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="absolute top-1 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <check-circle-icon class="w-5 h-5"/>
      <div>{{ local.adminSavedSuccessfully }}</div>
    </div>
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emptyFields();emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.admin }}</div>
      <label class="z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : firstName}">{{local.firstName}}</label>
      <input type="text" :placeholder="local.firstName" v-model="firstName"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : lastName}">{{local.lastName}}</label>
      <input type="text" :placeholder="local.lastName" v-model="lastName"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : username}">{{local.username}}</label>
      <input type="text" :placeholder="local.username" v-model="username"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : password}">{{local.password}}</label>
      <input type="text" :placeholder="local.password" v-model="password"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : phoneNumber}">{{local.phoneNumber}}</label>
      <input type="tel" :placeholder="local.phoneNumber" v-model="phoneNumber"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : email}">{{local.email}}</label>
      <input type="email" :placeholder="local.email" v-model="email"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : address}">{{local.address}}</label>
      <textarea :placeholder="local.address" v-model="address"
                class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex justify-end">
        <button
            @click="emptyFields();emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="saveAdmin">
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

let local = computed(() => {
  return useLocalization().getLocal
})

let firstName = ref('')
let lastName = ref('')
let username = ref('')
let password = ref('')
let phoneNumber = ref('')
let email = ref('')
let address = ref('')
const props = defineProps(['showDialog'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)

const emits = defineEmits(['closeDialog' , 'userAdded'])

const emptyFields = () => {
  firstName.value = ''
  lastName.value = ''
  username.value = ''
  password.value = ''
  phoneNumber.value = ''
  email.value = ''
  address.value = ''
}

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emptyFields()
    emits('closeDialog')
  }
}

const saveAdmin = () => {
  if (firstName.value && lastName.value && username.value && phoneNumber.value && address.value && password.value) {
    axios.post(`${useDataStore().getServerAddress}/users/create`,
        {
          firstName: firstName.value,
          lastName: lastName.value,
          email: email.value,
          phoneNumber: phoneNumber.value,
          address: address.value,
          role: 'Admin',
          password: password.value,
          username: username.value,
          enabled: true,
          totalFlow: 0,
          periodLength : 0,
          isIndefiniteExpirationTime: true,
          isIndefiniteFlow: true
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
      emits('userAdded')
    }).catch(() => {
      errorMessage.value = local.value.errorSavingAdmin
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    })
  } else {
    errorMessage.value = local.value.errorFieldsOfAdmin
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  }
}

</script>