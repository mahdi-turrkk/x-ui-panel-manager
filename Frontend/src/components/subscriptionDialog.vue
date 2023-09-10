<template>
  <div class="absolute h-full w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-40"
       v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subscription }}</div>
      <input type="text" :placeholder="local.title" v-model="title" :disabled="isRenew"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <input type="number" :placeholder="local.totalFlow" v-model="totalFlow"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <input type="number" :placeholder="local.ipLimit" v-model="ipLimit"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <input type="number" :placeholder="local.periodLength" v-model="periodLength"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex justify-end">
        <button
            @click="emits('closeDialog')"
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
import {computed, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon} from "@heroicons/vue/24/solid/index.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let subscriptionId = ref(undefined)
let totalFlow = ref('')
let ipLimit = ref('')
let title = ref('')
let periodLength = ref('')
const props = defineProps(['showDialog', 'subscription', 'isRenew'])

watch(() => props.subscription, (newVal) => {
  subscriptionId.value = props.subscription.id
  title.value = props.subscription.title
})

const emits = defineEmits(['closeDialog'])

let backdrop = ref(null)
const backdropClicked = (data)=>{
  if(data.target === backdrop.value){
    emits('closeDialog')
  }
}

</script>