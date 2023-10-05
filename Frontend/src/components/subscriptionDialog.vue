<template>
  <div
      class="absolute min-h-screen w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="absolute top-1 bg-error text-white flex rounded-xl px-4 py-1" v-if="showErrorMessage">
      <x-circle-icon class="w-5 h-5"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="absolute top-1 bg-success text-white flex rounded-xl px-4 py-1" v-if="showSuccessMessage">
      <check-circle-icon class="w-5 h-5"/>
      <div>{{ local.subscriptionSavedSuccessfully }}</div>
    </div>
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subscription }}</div>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
             :class="{'mt-0 opacity-100' : title}">{{ local.title }}</label>
      <input type="text" :placeholder="local.title" v-model="title" :disabled="type !== 'new'"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
             :class="{'mt-0 opacity-100' : totalFlow}">{{ local.totalFlow }}</label>
      <select type="number" v-model="totalFlow"
              class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150">
        <option value="" disabled selected>{{ local.totalFlow }}</option>
        <option v-for="totalFlow in totalFlows" class="my-4" :value="Number(totalFlow)">{{ totalFlow }} GB</option>
      </select>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
             :class="{'mt-0 opacity-100' : periodLength}">{{ local.periodLength }}</label>
      <select type="number" v-model="periodLength"
              class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150">
        <option value="" disabled selected>{{ local.periodLength }}</option>
        <option v-for="periodLength in periodLengths" class="my-4" :value="Number(periodLength)">{{ periodLength }} {{ local.days}}</option>
      </select>
      <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
             :class="{'mt-0 opacity-100' : number}" v-if="type === 'new'">{{ local.number }}</label>
      <input type="number" :placeholder="local.number" v-model="number" v-if="type === 'new'"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex justify-between z-0 px-2 pb-5 -mt-[40px] opacity-0 transition-all duration-200" :class="{'mt-0 opacity-100' : periodLength && totalFlow && number}" v-if="periodLength && totalFlow">
        <div>{{ local.price }}&nbsp :</div>
        <div>{{ plans.find((plan) => plan.totalFlow == totalFlow && plan.periodLength == periodLength).price * number }}</div>
      </div>
      <div class="flex justify-end">
        <button
            @click="emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="saveSubscription">
          {{ local.save }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {CheckCircleIcon, XCircleIcon, XMarkIcon} from "@heroicons/vue/24/solid/index.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import router from "../router/index.js";
import {useRoute, useRouter} from "vue-router";

let local = computed(() => {
  return useLocalization().getLocal
})

let subscriptionId = ref(undefined)
let totalFlow = ref('')
let ipLimit = ref('')
let title = ref('')
let periodLength = ref('')
let number = ref(1)
const props = defineProps(['showDialog', 'subscription', 'type'])

watch(() => props.subscription, () => {
  subscriptionId.value = props.subscription.id
  title.value = props.subscription.title
  totalFlow.value = ''
  ipLimit.value = props.subscription.ipLimit
  title.value = props.subscription.title
  periodLength.value = ''
})

const emits = defineEmits(['closeDialog' , 'subsAdded'])

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}

let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)
let errorMessage = ref('')
let totalFlows = ref([])
let periodLengths = ref([])
let plans = ref([])

const saveSubscription = () => {
  if (totalFlow.value && periodLength.value && (number.value || props.type === 'ReNew')) {
    if (props.type === 'ReNew') {
      axios.put(`${useDataStore().serverAddress}/subscriptions/update?updateType=ReNew&id=${props.subscription.id}`,
          {
            totalFlow: totalFlow.value,
            periodLength: periodLength.value
          },
          {
            headers: {
              Authorization: useDataStore().getToken
            }
          }
      ).then((response) => {
        showSuccessMessage.value = true
        setTimeout(() => {
          showSuccessMessage.value = false
          emits('closeDialog')
        }, 1000)
      }).catch((error) => {
        errorMessage = local.value.errorSavingSubscription
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 1000)
      })
    } else if (props.type === 'new') {
      axios.post(`${useDataStore().serverAddress}/subscriptions/create`,
          {
            title: title.value,
            totalFlow: totalFlow.value,
            periodLength: periodLength.value,
            numberSubscriptionsToGenerate: number.value
          },
          {
            headers: {
              Authorization: useDataStore().getToken
            }
          },
      ).then((response) => {
        showSuccessMessage.value = true
        setTimeout(() => {
          showSuccessMessage.value = false
          emits('closeDialog')
        }, 1000)
        emits('subsAdded')
      }).catch((error) => {
        errorMessage = local.value.errorSavingSubscription
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 1000)
      })
    }
  }
  else {
    errorMessage = local.value.errorFieldsOfSubscription
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 1000)
  }
}

const getPlans = () => {
  axios.get(`${useDataStore().getServerAddress}/plans/get-all` ,
      {
        headers : {
          Authorization : useDataStore().getToken
        }
      })
      .then((response) => {
        plans.value = response.data.content
        totalFlows.value = []
        periodLengths.value = []
        for (let i = 0 ; i < plans.value.length ; i++) {
          if (totalFlows.value.indexOf(plans.value[i].totalFlow) == -1) {
            totalFlows.value.push(plans.value[i].totalFlow)
          }
          if (periodLengths.value.indexOf(plans.value[i].periodLength) == -1) {
            periodLengths.value.push(plans.value[i].periodLength)
          }
        }
        totalFlows.value.sort((a,b) => a-b)
        periodLengths.value.sort((a,b) => a-b)
      })
}

watch(() => props.showDialog , ()=>{
  if(props.showDialog){
    getPlans()
  }
})
</script>