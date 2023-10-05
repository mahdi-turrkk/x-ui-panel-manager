<template>
  <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md my-2 text-info-3"
       :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
    <div class="w-0 hidden md:inline-block md:w-[10%]">{{ subscription.id }}</div>
    <div class="w-[30%] text-center">{{ subscription.startDate ? subscription.startDate.substring(0,10) : '' }}&nbsp;/&nbsp;{{ subscription.expireDate ? subscription.expireDate.substring(0,10) : '' }}</div>
    <div class="w-[30%] text-center" style="direction: ltr">{{ subscription.totalUsed ? subscription.totalUsed.toFixed(2) : '0.00' }}&nbsp;/&nbsp;{{ subscription.totalFlow ? subscription.totalFlow : '0.00'}} GB
    </div>
    <div class="w-[30%] md:w-[20%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
          v-if="subscription.status" @mouseenter="showDeactivateSubscriptionTag = true"
          @mouseleave="showDeactivateSubscriptionTag = false" @click="changeStatus(false)">{{ local.active }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateSubscriptionTag">{{ local.deactivate }}
          {{ local.subscription }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
          v-if="!subscription.status" @mouseenter="showActivateSubscriptionTag = true"
          @mouseleave="showActivateSubscriptionTag = false" @click="changeStatus(true)">{{ local.inactive }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateSubscriptionTag">{{ local.activate }}
          {{ local.subscription }}
        </div>
      </div>
    </div>
    <div class="w-[10%] flex justify-center relative" @mouseenter="showMenu = true" @mouseleave="showMenu = false" @click="showMenu = !showMenu">
        <div>
          <button class="flex justify-center items-center p-2">
            <cog8-tooth-icon class="w-6 h-6 text-info-3"/>
          </button>
        </div>
        <div class="absolute flex w-fit bg-background-1 p-2 rounded-xl" v-if="showMenu">
          <div class="relative cursor-pointer">
            <qr-code-icon class="w-6 h-6 text-success mx-1" @mouseenter="showUrlTag = true"
                          @mouseleave="showUrlTag = false" @click="emits('openLinkDialog' , subscription.link)"/>
            <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                 :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showUrlTag">{{ local.show }}
              {{ local.url }}
            </div>
          </div>
          <div class="relative cursor-pointer">
            <trash-icon class="w-6 h-6 text-error mx-1" @mouseenter="showDeleteTag = true"
                        @mouseleave="showDeleteTag = false"
                        @click="emits('openDeleteConfirmationDialog' , subscription)"/>
            <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                 :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeleteTag">{{ local.delete }} {{local.subscription}}
            </div>
          </div>
        </div>
    </div>
  </div>
</template>

<script setup>

import {useDataStore} from "../store/dataStore.js";
import {useLocalization} from "../store/localizationStore.js";
import {computed, ref} from "vue";
import {Cog8ToothIcon, QrCodeIcon, TrashIcon} from "@heroicons/vue/24/outline/index.js";
import axios from "axios";


const props = defineProps(['subscription'])
const emits = defineEmits(['openLinkDialog' , 'changeSubscriptionStatus' , 'openDeleteConfirmationDialog'])

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})
let local = computed(() => {
  return useLocalization().getLocal
})

let showActivateSubscriptionTag = ref(false)
let showDeactivateSubscriptionTag = ref(false)
let showUrlTag = ref(false)

const changeStatus = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/subscriptions/change-status?id=${props.subscription.id}&newStatus=${payload}` ,
      {},
      {
        headers : {
          authorization : useDataStore().getToken
        }
      }
  ).then((response) => {
    emits('changeSubscriptionStatus' , payload)
  }).catch((error) => {console.log(error)})
}

let showMenu = ref(false)
let showDeleteTag = ref(false)

</script>