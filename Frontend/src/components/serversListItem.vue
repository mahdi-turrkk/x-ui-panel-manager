<template>
  <div class="w-full rounded-xl text-xs overflow-hidden my-2">
    <div>
      <div
          class="bg-background-3 flex rounded-t-xl justify-between items-center px-4 py-4 z-20 relative cursor-pointer text-info-3"
          :class="{'rounded-b-xl' : server.id !== onboarding}">
        <div class="flex w-full items-center space-x-2">
          <div class="w-[25%] lg:w-[10%] no-scrollbar" @click="emits('setOnboarding' , server.id)">{{ server.id }}</div>
          <div class="hidden lg:inline-block lg:w-[30%] no-scrollbar" @click="emits('setOnboarding' , server.id)">
            {{ server.url }}
          </div>
          <div class="hidden lg:inline-block lg:w-[10%] no-scrollbar" @click="emits('setOnboarding' , server.id)">
            {{ server.username }}
          </div>
          <div class="hidden lg:inline-block lg:w-[30%] overflow-y-scroll no-scrollbar"
               @click="emits('setOnboarding' , server.id)">{{ server.password }}
          </div>
          <div class="w-[25%] lg:w-[10%] no-scrollbar">
            <div
                class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 text-success relative w-fit px-4 mx-auto"
                v-if="server.status" @mouseenter="showDeactivateTag = true"
                @mouseleave="showDeactivateTag = false" @click="changeStatus(false)">{{ local.active }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-error"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateTag">{{ local.deactivate }}
                {{ local.server }}
              </div>
            </div>
            <div
                class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 text-error relative w-fit px-4 mx-auto"
                v-if="!server.status" @mouseenter="showActivateTag = true" @mouseleave="showActivateTag = false"
                @click="changeStatus(true)">
              {{ local.inactive }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-success"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateTag">{{ local.activate }}
                {{ local.server }}
              </div>
            </div>
          </div>
          <div class="w-[50%] lg:w-[10%] flex justify-center no-scrollbar">
            <div class="relative" v-if="false">
              <ArrowPathIcon class="w-6 h-6 text-success mx-1" @mouseenter="showUpdateTag = true"
                             @mouseleave="showUpdateTag = false"/>
              <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showUpdateTag">{{ local.update }}
                {{ local.inbounds }}
              </div>
            </div>
            <div class="relative">
              <pencil-square-icon class="w-6 h-6 text-warning mx-1" @mouseenter="showEditTag = true"
                                  @mouseleave="showEditTag = false" @click="emits('openEditServerDialog' , server)"/>
              <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showEditTag">{{ local.edit }}
                {{ local.server }}
              </div>
            </div>
            <div class="relative" v-if="false">
              <plus-icon class="w-6 h-6 text-success mx-1" @mouseenter="showAddTag = true"
                         @mouseleave="showAddTag = false"/>
              <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                   :class="{'-left-8' : !isRtl , '-right-14' : isRtl}" v-if="showAddTag">{{ local.add }}
                {{ local.inbound }}
              </div>
            </div>
          </div>
        </div>
        <chevron-down-icon class="h-5 w-5 transition-all duration-300" @click="emits('setOnboarding' , server.id)"
                           :class="{'rotate-180' : onboarding === server.id}"/>
      </div>
      <div class="bg-background-2 px-8 md:px-12 py-4 transition-all duration-700 z-10 relative rounded-b-xl text-info-3"
           ref="expansionText"
           :style="{'margin-top' : marginTop}">
        <div class="flex flex-col lg:hidden space-y-2">
          <div class="flex">
            <div class="font-bold">{{ local.serverUrl }} :</div>
            <div class="mx-4">{{ server.url }}</div>
          </div>
          <div class="flex">
            <div class="font-bold">{{ local.username }} :</div>
            <div class="mx-4">{{ server.username }}</div>
          </div>
          <div class="flex">
            <div class="font-bold">{{ local.password }} :</div>
            <div class="mx-4">{{ server.password }}</div>
          </div>
        </div>
        <div class="text-sm font-bold mt-4">{{ local.inbounds }} :</div>
        <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md mb-2 mt-4"
             :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
          <div class="w-[25%] md:w-[20%] font-bold text-sm">{{ local.id }}</div>
          <div class="w-0 hidden md:inline-block md:w-[20%] text-center font-bold text-sm">{{ local.port }}</div>
          <div class="w-[45%] md:w-[35%] text-center font-bold text-sm">{{ local.title }}</div>
          <div class="w-[30%] md:w-[25%] text-center font-bold text-sm">{{ local.generatable }}</div>
        </div>
        <div class="h-full w-full flex justify-center items-center pt-12" v-if="inbounds.length === 0">
          <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
            <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-2 px-2">{{ local.noRecords }}</div>
          </div>
        </div>
        <inbound-list-item v-for="inbound in inbounds" :inbound="inbound" v-if="inbounds.length > 0"
                           @change-generatable="(payload) => {inbound.generatable = payload}"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ChevronDownIcon, ArrowPathIcon, PencilSquareIcon, PlusIcon} from "@heroicons/vue/24/outline";
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import InboundListItem from "./inboundListItem.vue";
import {useDataStore} from "../store/dataStore.js";
import axios from "axios";

let props = defineProps(['onboarding', 'server'])
const emits = defineEmits(['setOnboarding', 'openEditServerDialog', 'changeServerStatus'])

const expansionText = ref(null)



let marginTop = computed(() => {
  if (expansionText.value != null) {
    if (props.onboarding !== props.server.id)
      return `-${expansionText.value.offsetHeight}px`
    else return 0
  }
})

let local = computed(() => {
  return useLocalization().getLocal
})

let inbounds = ref([])

let showUpdateTag = ref(false)
let showEditTag = ref(false)
let showAddTag = ref(false)
let showActivateTag = ref(false)
let showDeactivateTag = ref(false)


let isRtl = computed(() => {
  return useLocalization().getDirection == 'rtl'
})

onMounted(() => {
  axios.get(`${useDataStore().getServerAddress}/inbounds/get-all?serverId=${props.server.id}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((resposnse) => {
    inbounds.value = resposnse.data.content
    emits('setOnboarding', props.server.id)
    setTimeout(() => {
      emits('setOnboarding', undefined)
    }, 1)
  }).catch((error) => console.log(error))
})

const changeStatus = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/servers/change-status?newStatus=${payload}&id=${props.server.id}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    emits('changeServerStatus', payload)
    if (!payload) {
      inbounds.value.forEach((inbound) => {
        inbound.generatable = payload
      })
    }
  }).catch((error) => {
    alert(error)
  })
}
</script>

<style>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
