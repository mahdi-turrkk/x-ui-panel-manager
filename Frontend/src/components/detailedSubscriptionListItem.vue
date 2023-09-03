<template>
  <div class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl my-2 text-info-3 text-xs">
    <div class="w-[20%] md:w-[10%]">{{ subscription.id }}</div>
    <div class="w-[30%] md:w-[15%] text-center">{{ subscription.title }}</div>
    <div class="hidden md:inline-block md:w-[22%] text-center">{{ subscription.startDate }}-{{ subscription.expireDate }}</div>
    <div class="hidden md:inline-block md:w-[23%] text-center" style="direction: ltr">{{
        (Number(subscription.totalUse) / (Math.pow(1024, 3))).toFixed(1)
      }}&nbsp;/&nbsp;{{ (Number(subscription.totalFlow) / (Math.pow(1024, 3))).toFixed(1) }} GB
    </div>
    <div class="w-[30%] md:w-[15%]">
      <div
          class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
          v-if="subscription.status" @mouseenter="showDeactivateSubscriptionTag = true"
          @mouseleave="showDeactivateSubscriptionTag = false">{{ local.active }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-error"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateSubscriptionTag">{{ local.deactivate }}
          {{ local.subscription }}
        </div>
      </div>
      <div
          class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
          v-if="!subscription.status" @mouseenter="showActivateSubscriptionTag = true"
          @mouseleave="showActivateSubscriptionTag = false">{{ local.inactive }}
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1 text-success"
             :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateSubscriptionTag">{{ local.activate }}
          {{ local.subscription }}
        </div>
      </div>
    </div>
    <div class="w-[20%] md:w-[15%] flex justify-center">
      <div class="relative cursor-pointer">
        <qr-code-icon class="w-6 h-6 text-success mx-1" @mouseenter="showUrlTag = true"
                      @mouseleave="showUrlTag = false" @click="emits('openLinkDialog' , subscription.link)"/>
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
             :class="{'-left-8' : !isRtl , '-right-14' : isRtl}" v-if="showUrlTag">{{ local.show }}
          {{ local.url }}
        </div>
      </div>
      <div class="relative cursor-pointer">
        <arrow-path-rounded-square-icon class="w-6 h-6 text-success mx-1" @mouseenter="showRenewTag = true"
                                        @mouseleave="showRenewTag = false" @click="emits('openRenewSubscriptionDialog' , subscription)"/>
        <div class="absolute -top-10 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
             :class="{'-left-8' : !isRtl , '-right-14' : isRtl}" v-if="showRenewTag">{{ local.renew }}
          {{ local.subscription }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useLocalization} from "../store/localizationStore.js";
import {computed, ref} from "vue";
import {QrCodeIcon , ArrowPathRoundedSquareIcon} from "@heroicons/vue/24/outline/index.js";


const props = defineProps(['subscription'])
const emits = defineEmits(['openRenewSubscriptionDialog' , 'openLinkDialog'])


let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})
let local = computed(() => {
  return useLocalization().getLocal
})

let showActivateSubscriptionTag = ref(false)
let showDeactivateSubscriptionTag = ref(false)
let showUrlTag = ref(false)
let showRenewTag = ref(false)

</script>

<style>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>