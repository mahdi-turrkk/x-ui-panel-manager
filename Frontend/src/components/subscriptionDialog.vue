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
      <div>{{ local.subscriptionSavedSuccessfully }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subscription }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar pt-2">
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : title}">{{ local.title }}</label>
        <input type="text" :placeholder="local.title" v-model="title" :disabled="type !== 'new'"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : totalFlow}">{{ local.totalFlow }}</label>
        <select type="number" v-model="totalFlow" v-if="userType === 'Customer'"
                class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150">
          <option value="" disabled selected>{{ local.totalFlow }}</option>
          <option v-for="totalFlow in totalFlows" class="my-4" :value="Number(totalFlow)">{{ totalFlow }} GB</option>
        </select>
        <input type="number" :placeholder="local.totalFlow" v-model="totalFlow"
               v-else-if="userType === 'Admin' || userType === 'SuperCustomer'"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : periodLength}">{{ local.periodLength }}</label>
        <select type="number" v-model="periodLength" v-if="userType === 'Customer'"
                class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150">
          <option value="" disabled selected>{{ local.periodLength }}</option>
          <option v-for="periodLength in periodLengths" class="my-4" :value="Number(periodLength)">{{ periodLength }}
            {{ local.days }}
          </option>
        </select>
        <input type="number" :placeholder="local.periodLength" v-model="periodLength"
               v-else-if="userType === 'Admin' || userType === 'SuperCustomer'"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[40px] opacity-0 transition-all duration-200"
               :class="{'mt-0 opacity-100' : number}" v-if="type === 'new'">{{ local.number }}</label>
        <input type="number" :placeholder="local.number" v-model="number" v-if="type === 'new'"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      </div>
      <div class="flex items-center mt-2"
           :class="{'justify-between' : periodLength && totalFlow , 'justify-end' : !periodLength || !totalFlow }">
        <div class="flex z-0 opacity-0 transition-all duration-200"
             :class="{'opacity-100' : periodLength && totalFlow && number}"
             v-if="periodLength && totalFlow">
          <div>{{ local.price }}&nbsp :</div>
          <div>
            {{ price }}
          </div>
        </div>
        <div class="flex">
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
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
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

let subscriptionId = ref(undefined)
let totalFlow = ref('')
let ipLimit = ref('')
let title = ref('')
let periodLength = ref('')
let number = ref(1)
const props = defineProps(['showDialog', 'subscription', 'type', 'userType' , 'pricePerGb'])

watch(() => props.subscription, () => {
  subscriptionId.value = props.subscription.id
  title.value = props.subscription.title
  totalFlow.value = ''
  ipLimit.value = props.subscription.ipLimit
  title.value = props.subscription.title
  periodLength.value = ''
})

const emits = defineEmits(['closeDialog', 'subsAdded'])

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
    if (totalFlow.value < 0 || (periodLength.value <= 0 && props.type === 'new')|| (periodLength.value < 0 && props.type === 'ReNew') || (number.value <= 0 && props.type === 'new')) {
      errorMessage = local.value.invalidFlowOrPeriod
      showErrorMessage.value = true
      setTimeout(() => {
        showErrorMessage.value = false
      }, 1000)
    } else {
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
  } else {
    errorMessage = local.value.errorFieldsOfSubscription
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 1000)
  }
}

const getPlans = () => {
  axios.get(`${useDataStore().getServerAddress}/plans/get-all`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      })
      .then((response) => {
        plans.value = response.data.content
        totalFlows.value = []
        periodLengths.value = []
        for (let i = 0; i < plans.value.length; i++) {
          if (totalFlows.value.indexOf(plans.value[i].totalFlow) == -1) {
            totalFlows.value.push(plans.value[i].totalFlow)
          }
          if (periodLengths.value.indexOf(plans.value[i].periodLength) == -1) {
            periodLengths.value.push(plans.value[i].periodLength)
          }
        }
        totalFlows.value.sort((a, b) => a - b)
        periodLengths.value.sort((a, b) => a - b)
      })
}

watch(() => props.showDialog, () => {
  if (props.showDialog && props.userType === 'Customer') {
    getPlans()
  }
})

let price = computed(() => {
  if(props.userType === 'Customer')
    return plans.value.find((plan) => plan.totalFlow == totalFlow.value && plan.periodLength == periodLength.value).price * number.value
  else if(props.userType === 'SuperCustomer') {
    return (Math.ceil((periodLength.value/30)-1)*10000 + props.pricePerGb*totalFlow.value) * number.value
  }
})
</script>


