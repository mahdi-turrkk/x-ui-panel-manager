<template>
  <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md my-2"
       :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
    <div class="w-0 hidden md:inline-block md:w-[15%]">{{ inbound.id }}</div>
    <div class="w-0 hidden md:inline-block md:w-[10%] text-center">{{ inbound.port }}</div>
    <div class="w-[40%] md:w-[30%] text-center">{{ inbound.title }}</div>
    <div class="w[30%] md:w-[22%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
          v-if="inbound.status" @mouseenter="showDeactivateInboundTag = true"
          @mouseleave="showDeactivateInboundTag = false">{{ local.active }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateInboundTag">{{ local.deactivate }}
          {{ local.inbound }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
          v-if="!inbound.status" @mouseenter="showActivateInboundTag = true"
          @mouseleave="showActivateInboundTag = false">{{ local.inactive }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateInboundTag">{{ local.activate }}
          {{ local.inbound }}
        </div>
      </div>
    </div>
    <div class="w-[30%] md:w-[23%]">
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

const chanageGeneratable = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/inbounds/change-inbound-generatable`,
      {
        inboundId: props.inbound.id,
        generatable: payload
      },
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    props.inbound.generatable = payload
  }).catch((error) => {
    console.log(error)
  })
}

</script>