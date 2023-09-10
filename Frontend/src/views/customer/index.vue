<template>
  <div class="min-h-screen min-w-screen text-info-3">
    <sub-lookup-dialog :show-dialog="showLookupDialog" @close-dialog="showLookupDialog = false"/>
    <div class="col-span-12 relative z-20 bg-background-3">
      <div class="px-4 py-4">
        <div class="flex items-center justify-between">
          <div class="flex">
            <div class="relative px-2 text-lg">
              <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
                   @click="showLangMenu = !showLangMenu">{{ useLocalization().getFlag }}
                {{ useLocalization().getLanguage.toUpperCase() }}
              </div>
              <div class="absolute left-0 rounded-xl w-max bg-background-3"
                   v-if="showLangMenu">
                <div class="flex flex-col rounded-xl bg-primary-1 bg-opacity-20">
                  <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                       @click="changeLanguage(['🇮🇷','fa' , 'rtl'])">🇮🇷 FA
                  </div>
                  <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                       @click="changeLanguage(['🇺🇸','en' , 'ltr'])">🇺🇸 EN
                  </div>
                </div>
              </div>
            </div>
            <button @click="changeThemeStatus"
                    class="p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200">
              <sun-icon class="w-6 h-6 text-info-3" v-if="isDark"/>
              <moon-icon class="w-6 h-6 text-info-3" v-if="!isDark"/>
            </button>
            <button @click="showLookupDialog = true"
                    @mouseenter="showLookupTag = true" @mouseleave="showLookupTag = false"
                    class="relative p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200 lg:hidden">
              <magnifying-glass-icon class="w-6 h-6 text-info-3"/>
              <div class="absolute -bottom-6 bg-background-3 text-info-3 rounded-lg py-1 px-4 text-sm w-max"
                   v-if="showLookupTag">{{ local.subLookUp }}
              </div>
            </button>
          </div>
          <img src="/src/assets/logo-white.png" class="h-10 w-10 cursor-pointer" @click="router.push('/')" v-if="useDataStore().getDarkStatus">
          <img src="/src/assets/logo-black.png" class="h-10 w-10 cursor-pointer" @click="router.push('/')" v-else>
        </div>
      </div>
    </div>
    <div class="p-4 grid grid-cols-1 lg:grid-cols-6 xl:grid-cols-5">
      <div class="hidden lg:inline-block lg:col-span-2 xl:col-span-1" :class="{'pl-4' : isRtl , 'pr-4' : !isRtl}">
        <div
            class="bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4">
          <div class="font-bold text-sm md:text-lg text-center">{{ local.subLookUp }}</div>
          <div class="flex relative w-full">
            <input type="text" :placeholder="local.subscriptionUrl"
                   class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
                   :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
            />
            <magnifying-glass-icon class="h-5 w-5 text-info-2 absolute top-3 cursor-pointer hover:text-info-1"
                                   :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
                                   @click="searchSubscription"/>
          </div>
          <div class="font-bold text-center flex flex-col space-y-2 px-6"
               v-if="lookupSubscription.id != undefined">
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
              {{ lookupSubscription.startDate }}
            </div>
            </div>
            <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                      :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ lookupSubscription.expireDate }}
            </div>
            </div>
            <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                     :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ (Number(lookupSubscription.totalUsed) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
            </div>
            </div>
            <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                     :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ (Number(lookupSubscription.totalFlow) / Math.pow(1024, 3)).toFixed(1) }}&nbsp;&nbsp;&nbsp;GB
            </div>
            </div>
            <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                                  :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
              {{ lookupSubscription.status }}
            </div>
            </div>
          </div>
          <div class="text-info-2 px-6 pb-14 text-center"
               v-if="lookupSubscription.id === undefined">
            <document-magnifying-glass-icon class="max-w-56 max-h-56 mx-auto"/>
            <div class="text-xs md:text-sm lg:text-base">{{ local.enterSubToSearch }}</div>
          </div>
        </div>
      </div>
      <div class="lg:col-span-4 -mt-2">
        <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
        <subscription-dialog :show-dialog="showSubscriptionDialog" @close-dialog="showSubscriptionDialog = false"
                             :subscription="subscription" :is-renew="isRenew"/>
        <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
          <div class="text-info-3 font-bold text-lg">{{ local.subscriptions }}</div>
          <button
              @click="()=>{subscription = {};isRenew = false ;showSubscriptionDialog = true}"
              class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
            <plus-icon class="w-4 h-4"/>
            {{ local.add }} {{ local.subscription }}
          </button>
        </div>
        <subscriptions-list :subscriptions="subscriptions" @open-renew-subscription-dialog="openRenewSubscriptionDialog"
                            @open-link-dialog="openLinkDialog"/>
        <div class="flex mt-6">
          <div
              class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
              v-for="i in pages" :class="{'bg-opacity-50' : onboarding == i}" @click="onboarding = i">{{ i }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import router from "../../router/index.js";
import {useLocalization} from "../../store/localizationStore.js";
import {
  DocumentMagnifyingGlassIcon,
  MagnifyingGlassIcon,
  MoonIcon,
  PlusIcon,
  SunIcon
} from "@heroicons/vue/24/outline/index.js";
import {computed, reactive, ref} from "vue";
import {useDataStore} from "../../store/dataStore.js";
import SubscriptionsList from "../../components/subscriptionsList.vue";
import SubscriptionDialog from "../../components/subscriptionDialog.vue";
import SubscriptionLinkDialog from "../../components/subscriptionLinkDialog.vue";
import SubLookupDialog from "../../components/subLookupDialog.vue";

let isDark = computed(() => {
  return useDataStore().getDarkStatus
})

const changeThemeStatus = () => {
  useDataStore().changeDarkStatus()
}

let logoSrc = computed(() => {
  return useDataStore().getDarkStatus ? '../src/assets/logo-white.png' : '../src/assets/logo-black.png'
})

let showLangMenu = ref(false)
let changeLanguage = (payload) => {
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
}

let local = computed(() => useLocalization().getLocal)

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

let subscriptions = ref([
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 1'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 2'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 3'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 4'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 5'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 6'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 7'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 8'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 9'
  },
  {
    id: '3242',
    totalUse: '324532567890',
    totalFlow: '456788034544',
    status: true,
    title: 'mobile-shahr',
    startDate: '2023/09/03',
    expireDate: '2023/10/05',
    ipLimit: 2,
    periodLength: 30,
    link: 'this is test link for qr generating 10'
  },

])

const searchSubscription = () => {
}

let subscription = reactive(undefined)
let link = ref('')
let isRenew = ref(false)
let pages = ref(10)
let onboarding = ref(1)
let showSubscriptionDialog = ref(false)
let showLinkDialog = ref(false)
const openRenewSubscriptionDialog = (payload) => {
  subscription = payload
  isRenew.value = true
  showSubscriptionDialog.value = true
}

const openLinkDialog = (payload) => {
  link.value = payload
  showLinkDialog.value = true
}

let showLookupTag = ref(false)
let showLookupDialog = ref(false)

</script>