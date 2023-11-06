<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.userDetail }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <div class="flex font-bold text-sm md:text-lg text-center">{{
            userDetail.firstName + " " + userDetail.lastName
          }}
        </div>
        <div class="flex font-bold text-sm md:text-lg text-center">{{ local.debt }}&nbsp;:&nbsp;
          <div
              class="font-normal"
              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ userDetail.debtAmount + "0" }}&nbsp;&nbsp;﷼
          </div>
        </div>
        <div class="flex font-bold text-sm md:text-lg text-center">{{ local.startDate }}&nbsp;:&nbsp;
          <div
              class="font-normal"
              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ userDetail.startDateTime.substring(0, 10) }}
          </div>
        </div>
        <div class="flex font-bold text-sm md:text-lg text-center" v-if="userDetail.expirationDateTime">
          {{ local.expireDate }}&nbsp;:&nbsp;
          <div
              class="font-normal"
              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ userDetail.expirationDateTime.substring(0, 10) }}
          </div>
        </div>
        <div class="flex font-bold text-sm md:text-lg text-center" v-if="userDetail.totalFlow">{{ local.remaining }}&nbsp;:&nbsp;
          <div
              class="font-normal" style="direction: ltr"
              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ userDetail.totalFlow - userDetail.totalUsed }} GB
          </div>
        </div>
        <div class="flex font-bold text-sm md:text-lg text-center" v-if="!userDetail.totalFlow">{{ local.totalUsed }}&nbsp;:&nbsp;
          <div
              class="font-normal" style="direction: ltr"
              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ userDetail.totalUsed }} GB
          </div>
        </div>
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

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

const props = defineProps(['showDialog' , 'userDetail'])

const emits = defineEmits(['closeDialog'])

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}
</script>


