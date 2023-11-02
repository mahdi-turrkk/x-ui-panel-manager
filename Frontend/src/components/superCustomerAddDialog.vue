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
      <div>{{ local.customerSavedSuccessfully }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emptyFields();emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.customer }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <label class="z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : firstName}">{{ local.firstName }}</label>
        <input type="text" :placeholder="local.firstName" v-model="firstName"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : lastName}">{{ local.lastName }}</label>
        <input type="text" :placeholder="local.lastName" v-model="lastName"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : username}">{{ local.username }}</label>
        <input type="text" :placeholder="local.username" v-model="username"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : totalFlow}">{{ local.totalFlow }}</label>
        <input type="number" :placeholder="local.totalFlow + '(' + 'GB' + ')'" v-model="totalFlow"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : periodLength}">{{ local.periodLength }}</label>
        <input type="number" :placeholder="local.periodLength" v-model="periodLength"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : pricePerUse}">{{ local.pricePerUse }}</label>
        <input type="number" :placeholder="local.pricePerUse" v-model="pricePerUse"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : password}">{{ local.password }}</label>
        <input type="text" :placeholder="local.password" v-model="password"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : phoneNumber}">{{ local.phoneNumber }}</label>
        <input type="tel" :placeholder="local.phoneNumber" v-model="phoneNumber"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : email}">{{ local.email }}</label>
        <input type="email" :placeholder="local.email" v-model="email"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : address}">{{ local.address }}</label>
        <textarea :placeholder="local.address" v-model="address"
                  class="min-h-[60px] z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      </div>
      <div class="flex justify-end mt-2">
        <button
            @click="emptyFields();emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="saveCustomer">
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

let firstName = ref('')
let lastName = ref('')
let username = ref('')
let password = ref('')
let phoneNumber = ref('')
let email = ref('')
let address = ref('')
let totalFlow = ref('')
let periodLength = ref('')
let isIndefiniteFlow = ref(false)
let isIndefiniteExpirationTime = ref(false)
let pricePerUse = ref("")
const props = defineProps(['showDialog'])
let errorMessage = ref('')
let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)

const emits = defineEmits(['closeDialog', 'userAdded'])

const emptyFields = () => {
  firstName.value = ''
  lastName.value = ''
  username.value = ''
  password.value = ''
  phoneNumber.value = ''
  email.value = ''
  address.value = ''
  totalFlow.value = ''
  periodLength.value = ''
  pricePerUse.value = ''
  isIndefiniteExpirationTime.value = false
  isIndefiniteFlow.value = false
}

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emptyFields()
    emits('closeDialog')
  }
}

const saveCustomer = () => {
  if (firstName.value && lastName.value && username.value && phoneNumber.value && address.value && password.value && pricePerUse) {
    if (!periodLength.value || Number(periodLength.value) == 0) {
      periodLength.value = 0
      isIndefiniteExpirationTime.value = true
    }
    if (!totalFlow.value || Number(totalFlow.value) == 0) {
      totalFlow.value = 0
      isIndefiniteFlow.value = true
    }
    axios.post(`${useDataStore().getServerAddress}/users/create`,
        {
          firstName: firstName.value,
          lastName: lastName.value,
          email: email.value,
          phoneNumber: phoneNumber.value,
          address: address.value,
          role: 'SuperCustomer',
          password: password.value,
          username: username.value,
          enabled: true,
          totalFlow: totalFlow.value,
          periodLength: periodLength.value,
          isIndefiniteFlow: isIndefiniteFlow.value,
          isIndefiniteExpirationTime: isIndefiniteExpirationTime.value,
          pricePerUse: pricePerUse.value
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
      errorMessage.value = local.value.errorSavingCustomer
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    })
  } else {
    errorMessage.value = local.value.errorFieldsOfCustomer
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  }
}
</script>


