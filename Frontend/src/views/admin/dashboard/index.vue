<template>
  <admin-layout>
    <div class="text-info-3 font-bold text-lg px-4 mt-4">{{ local.dashboard }}</div>
    <div class="grid grid-cols-1 md:grid-cols-2">
      <div class="mt-6 bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.subLookUp }}</div>
        <div class="flex relative w-full">
          <input type="text" :placeholder="local.subscriptionUrl"
                 class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
                 :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
          />
          <magnifying-glass-icon class="h-5 w-5 text-info-2 absolute top-3 cursor-pointer hover:text-info-1"
                                 :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
                                 @click="searchSubscription"/>
        </div>
        <div class="font-bold text-center flex flex-col space-y-2 px-6 relative"
             :class="{'text-background-3 select-none' : subscription.id === undefined}">
          <div class="flex">{{ local.id }}&nbsp;:&nbsp;<div class="font-normal"
                                                            :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.id }}
          </div>
          </div>
          <div class="flex">{{ local.title }}&nbsp;:&nbsp;<div class="font-normal"
                                                               :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.title }}
          </div>
          </div>
          <div class="flex">{{ local.startDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.startDate }}
          </div>
          </div>
          <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                    :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.expireDate }}
          </div>
          </div>
          <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ (Number(subscription.totalUsed) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ (Number(subscription.totalFlow) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                                :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
            {{ subscription.status }}
          </div>
          </div>
          <div class="absolute top-0 bottom-0 right-0 left-0 text-info-2 px-6 md:px-10 pb-14"
               v-if="subscription.id === undefined">
            <document-magnifying-glass-icon class="w-full h-full"/>
            <div class="text-xs md:text-sm">{{ local.enterSubToSearch }}</div>
          </div>
        </div>
      </div>
      <div class="mt-6 bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.database }}</div>
        <div class="grid grid-cols-1 md:grid-cols-2 h-full py-4 px-4">
          <div
              class="mx-2 my-2 rounded-xl py-4 border border-secondary-3 px-10 flex flex-col justify-center items-center text-secondary-3 brightness-75 hover:brightness-100 transition-all duration-200 cursor-pointer">
            <arrow-up-tray-icon class="max-w-20 max-h-20"/>
            <div class="text-sm mt-4">{{ local.upload }} {{ local.database }}</div>
          </div>
          <div
              class="mx-2 my-2 rounded-xl py-4 border border-primary-1 px-10 flex flex-col justify-center items-center text-primary-1 brightness-75 hover:brightness-150 transition-all duration-200 cursor-pointer">
            <arrow-down-tray-icon class="max-w-20 max-h-20"/>
            <div class="text-sm mt-4">{{ local.download }} {{ local.database }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="w-full mt-6">
      <div
          class="bg-background-3 text-info-3 rounded-xl px-4 py-4 pb-8 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.activitySummary }}</div>
        <div class="flex relative w-full">
          <div class="w-full grid grid-cols-1 md:grid-cols-2 mt-6">
            <div class="font-bold text-center flex flex-col space-y-4 px-6 relative">
              <div class="flex">{{ local.lastMonthSells }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                                  :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.lastMonthSells }}
              </div>
              </div>
              <div class="flex">{{ local.totalSells }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.totalSells }}
              </div>
              </div>
              <div class="flex">{{ local.totalUpload }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                               style="direction: ltr"
                                                                               :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ (Number(data.totalUpload) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
              </div>
              </div>
              <div class="flex">{{ local.totalDownload }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                                 style="direction: ltr"
                                                                                 :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ (Number(data.totalDownload) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
              </div>
              </div>
            </div>
            <div class="w-56 mx-auto -mt-8 hidden md:inline-block"><PresentationChartLineIcon class="text-info-2"/></div>
          </div>
        </div>
      </div>
    </div>
  </admin-layout>
</template>
<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {computed, reactive} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import {
  MagnifyingGlassIcon,
  DocumentMagnifyingGlassIcon,
  ArrowUpTrayIcon,
  ArrowDownTrayIcon,
  PresentationChartLineIcon
} from "@heroicons/vue/24/outline/index.js";

let local = computed(() => useLocalization().getLocal)
let isRtl = computed(() => useLocalization().getDirection === 'rtl')

let subscription = reactive({
  id: undefined,
  title: 'shahr-mobile',
  startDate: '2023/09/04',
  expireDate: '2023/10/05',
  totalUsed: '34534583905',
  totalFlow: '57434658206',
  status: true
})

let data = reactive({
  lastMonthSells: 154,
  totalSells: 1273,
  totalUpload: 1587542336733,
  totalDownload: 5148756925692
})

const searchSubscription = () => {
}
</script>