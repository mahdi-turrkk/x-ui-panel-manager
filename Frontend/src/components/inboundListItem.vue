<template>
  <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md my-2"
       :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
    <div class="w-[25%] md:w-[20%]">{{ inbound.id }}</div>
    <div class="w-0 hidden md:inline-block md:w-[20%] text-center">{{ inbound.port }}</div>
    <div class="w-[45%] md:w-[35%] text-center">{{ inbound.remark }}</div>
    <div class="w-[30%] md:w-[25%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
          v-if="inbound.generatable" @mouseenter="showDeactivateGeneratableTag = true"
          @mouseleave="showDeactivateGeneratableTag = false" @click="chanageGeneratable(false)">{{ local.active }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateGeneratableTag">{{
            local.deactivate
          }}
          {{ local.generatable }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
          v-if="!inbound.generatable" @mouseenter="showActivateGeneratableTag = true"
          @mouseleave="showActivateGeneratableTag = false" @click="chanageGeneratable(true)">{{ local.inactive }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateGeneratableTag">{{ local.activate }}
          {{ local.generatable }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {useDataStore} from "../store/dataStore.js";
import {useLocalization} from "../store/localizationStore.js";
import {computed, ref} from "vue";
import axios from "axios";

const props = defineProps(['inbound'])
const emits = defineEmits(['changeGeneratable'])

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})
let local = computed(() => {
  return useLocalization().getLocal
})

let showActivateInboundTag = ref(false)
let showDeactivateInboundTag = ref(false)
let showActivateGeneratableTag = ref(false)
let showDeactivateGeneratableTag = ref(false)
let isStatusChangeInProgress = ref(false)

const chanageGeneratable = (payload) => {
  if (!isStatusChangeInProgress.value){
    isStatusChangeInProgress.value = true
    axios.put(`${useDataStore().getServerAddress}/inbounds/change-inbound-generatable?inboundId=${props.inbound.id}&generatable=${payload}`,
        {},
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      emits('changeGeneratable' , payload)
      isStatusChangeInProgress.value = false
    }).catch((error) => {
      console.log(error)
      isStatusChangeInProgress.value = false
    })
  }
}

</script>