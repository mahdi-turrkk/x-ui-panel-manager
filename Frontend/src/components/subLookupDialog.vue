<template>
  <div class="absolute h-full w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-40"
       v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative mx-4">
      <div
          @click="lookupSubscription = {};emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="font-bold text-sm text-center mb-4">{{ local.subLookUp }}</div>
      <div class="flex relative w-full">
        <input type="text" :placeholder="local.subscriptionUrl" v-model="subLink"
               class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
               :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
        />
        <magnifying-glass-icon class="h-5 w-5 text-info-2 absolute top-3 cursor-pointer hover:text-info-1"
                               :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
                               @click="searchSubscription"/>
      </div>
      <div class="font-bold text-center flex flex-col space-y-2 px-2 relative text-sm"
           v-if="showSubDetail">
        <div class="flex">{{ local.id }}&nbsp;:&nbsp;<div class="font-normal"
                                                          :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.id }}
        </div>
        </div>
        <div class="flex">{{ local.title }}&nbsp;:&nbsp;<div class="font-normal"
                                                             :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.title }}
        </div>
        </div>
        <div class="flex">{{ local.startDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                 :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.startDate ? lookupSubscription.startDate.substring(0,10) : '' }}
        </div>
        </div>
        <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                  :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.expireDate ? lookupSubscription.expireDate.substring(0,10) : '' }}
        </div>
        </div>
        <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                 :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.totalUsed ? lookupSubscription.totalUsed : '0.00'}}&nbsp;&nbsp;&nbsp;GB
        </div>
        </div>
        <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                 :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
          {{ lookupSubscription.totalFlow ? lookupSubscription.totalFlow : '0.00'}}&nbsp;&nbsp;&nbsp;GB
        </div>
        </div>
        <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                              :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
          {{ lookupSubscription.status ? local.active : local.inactive }}
        </div>
        </div>
      </div>
      <div class="text-info-2 px-6 md:px-10 pb-14"
           v-if="!showSubDetail">
        <document-magnifying-glass-icon class="w-36 h-36 mx-auto"/>
        <div class="text-xs md:text-sm">{{ local.enterSubToSearch }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon} from "@heroicons/vue/24/solid/index.js";
import {DocumentMagnifyingGlassIcon, MagnifyingGlassIcon} from "@heroicons/vue/24/outline/index.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let isRtl = computed(() => useLocalization().getDirection === 'rtl')
let lookupSubscription = reactive({
  id: undefined,
  title: 'shahr-mobile',
  startDate: '2023/09/04',
  expireDate: '2023/10/05',
  totalUsed: '34534583905',
  totalFlow: '57434658206',
  status: true
})

let subLink = ref('')
let showSubDetail = ref(false)

const searchSubscription = ()=>{
  if(subLink.value){
    axios.get(`${useDataStore().getServerAddress}/subscriptions/report?subLink=${subLink.value}` ,
        {
          headers : {
            Authorization : useDataStore().getToken
          }
        }
    ).then((response) => {
      lookupSubscription = response.data
      showSubDetail.value = true
    }).catch((error) => console.log(error))
  }
}


const props = defineProps(['showDialog'])
const emits = defineEmits(['closeDialog'])

let backdrop = ref(null)
const backdropClicked = (data)=>{
  if(data.target === backdrop.value){
    lookupSubscription = {}
    emits('closeDialog')
  }
}

</script>