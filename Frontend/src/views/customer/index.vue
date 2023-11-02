<template>
  <div class="min-h-screen min-w-screen text-info-3">
    <sub-lookup-dialog :show-dialog="showLookupDialog" @close-dialog="showLookupDialog = false" @renew-sub="(payload)=> {subscription = payload;subEditType='ReNew';showSubscriptionDialog=true}"/>
    <change-password-dialog :show-dialog="showChangePasswordDialog" @close-dialog="showChangePasswordDialog = false"
                            :isSelf="true"/>
    <subscription-renew-history-dialog user-type="Customer" :show-dialog="showRenewHistoryDialog"
                                       @close-dialog="showRenewHistoryDialog = false" :subscription="subscription"/>
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
            <button @click="showLookupDialog = true"
                    @mouseenter="showLookupTag = true" @mouseleave="showLookupTag = false"
                    class="relative p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200 lg:hidden">
              <i class="pi pi-search text-xl text-info-3"/>
              <div class="absolute -bottom-6 bg-background-3 text-info-3 rounded-lg py-1 px-4 text-sm w-max"
                   v-if="showLookupTag">{{ local.subLookUp }}
              </div>
            </button>
          </div>
          <div class="flex justify-end">
            <div class="relative px-2 text-lg">
              <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
                   @click="showSettingMenu = !showSettingMenu">
                <i class="pi pi-cog text-xl"/>
              </div>
              <div class="absolute left-0 rounded-xl w-max bg-background-3"
                   :class="{'left-0' : isRtl , '-left-20' : !isRtl}"
                   v-if="showSettingMenu">
                <div class="flex flex-col rounded-xl bg-primary-1 bg-opacity-20">
                  <div
                      class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl text-xs md:text-sm flex items-center"
                      @click="showSettingMenu = false;showChangePasswordDialog = true">
                    <i class="pi pi-lock text-lg mx-2"/>
                    {{ local.changePassword }}
                  </div>
                  <div
                      class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl text-xs md:text-sm flex items-center"
                      @click="changeThemeStatus">
                    <i class="pi pi-sun text-lg mx-2" v-if="isDark"/>
                    <i class="pi pi-moon text-lg mx-2" v-if="!isDark"/>
                    {{ local.changeTheme }}
                  </div>
                </div>
              </div>
            </div>
            <img src="/src/assets/logo-white.webp" class="h-10 w-10 cursor-pointer"
                 @click="logOut"
                 v-if="useDataStore().getDarkStatus">
            <img src="/src/assets/logo-black.webp" class="h-10 w-10 cursor-pointer" @click="logOut" v-else>
          </div>
        </div>
      </div>
    </div>
    <div class="p-4 grid grid-cols-1 lg:grid-cols-6 xl:grid-cols-5">
      <div class="hidden lg:inline-block lg:col-span-2 xl:col-span-1" :class="{'pl-4' : isRtl , 'pr-4' : !isRtl}">
        <div
            class="bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4">
          <div class="font-bold text-sm md:text-lg text-center">{{ local.subLookUp }}</div>
          <div class="flex relative w-full">
            <input type="text" :placeholder="local.subscriptionUrl" v-model="subLink"
                   class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
                   :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
            />
            <i class="pi pi-search text-lg z-20 text-info-2 absolute top-3 cursor-pointer hover:text-info-1"
               :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
               @click="searchSubscription"/>
          </div>
          <div class="font-bold text-center flex flex-col space-y-2 px-6"
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
              {{ lookupSubscription.startDate ? lookupSubscription.startDate.substring(0, 10) : '' }}
            </div>
            </div>
            <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                      :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ lookupSubscription.expireDate ? lookupSubscription.expireDate.substring(0, 10) : '' }}
            </div>
            </div>
            <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                     :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ lookupSubscription.totalUsed ? Number(lookupSubscription.totalUsed).toFixed(2) : '0.00' }}&nbsp;&nbsp;&nbsp;GB
            </div>
            </div>
            <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                     :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ lookupSubscription.totalFlow ? lookupSubscription.totalFlow : '0.00' }}&nbsp;&nbsp;&nbsp;GB
            </div>
            </div>
            <div class="flex">{{ local.payStatus }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                     :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ lookupSubscription.markAsPaid ? local.paid : local.paid }}
            </div>
            </div>
            <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                                  :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
              <div
                  class="text-xs md:text-sm bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 px-4 w-min text-success relative mx-auto cursor-pointer"
                  v-if="showSubDetail && lookupSubscription.status" @mouseenter="showDeactivateSubscriptionTag = true"
                  @mouseleave="showDeactivateSubscriptionTag = false" @click="changeStatus(false)">{{ local.active }}
                <div class="absolute -top-10 bg-background-3 w-max rounded-xl px-2 py-1 text-error"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateSubscriptionTag">{{
                    local.deactivate
                  }}
                  {{ local.subscription }}
                </div>
              </div>
              <div
                  class="text-xs md:text-sm bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 px-4 w-min text-error relative mx-auto cursor-pointer"
                  v-if="showSubDetail && !lookupSubscription.status" @mouseenter="showActivateSubscriptionTag = true"
                  @mouseleave="showActivateSubscriptionTag = false" @click="changeStatus(true)">{{ local.inactive }}
                <div class="absolute -top-10 bg-background-3 w-max rounded-xl px-2 py-1 text-success"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateSubscriptionTag">{{ local.activate }}
                  {{ local.subscription }}
                </div>
              </div>
            </div>
            </div>
            <div class="flex justify-end pt-4">
              <button
                  class="outline-none border-2 border-success rounded-xl bg-success bg-opacity-50 text-success px-4 py-1 flex space-x-1 items-center text-sm"
                  @click="subscription = lookupSubscription;subEditType='ReNew';showSubscriptionDialog=true"
              >
                <i class="pi pi-refresh font-bold mx-1"></i>
                {{local.renew}} {{local.subscription}}
              </button>
            </div>
          </div>
          <div class="text-info-2 px-6 pb-14 text-center"
               v-if="!showSubDetail">
            <i class="pi pi-file text-[80px] mt-4 mb-8 mx-auto"/>
            <div class="text-xs md:text-sm lg:text-base">{{ local.enterSubToSearch }}</div>
          </div>
        </div>
        <div class="bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4 mt-4">
          <div class="flex font-bold text-sm md:text-lg text-center">{{ userDetail.firstName + " " + userDetail.lastName }}</div>
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
              {{ userDetail.startDateTime.substring(0,10) }}
            </div>
          </div>
          <div class="flex font-bold text-sm md:text-lg text-center" v-if="userDetail.expirationDateTime">{{ local.expireDate }}&nbsp;:&nbsp;
            <div
                class="font-normal"
                :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
              {{ userDetail.expirationDateTime.substring(0,10) }}
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
      <div class="lg:col-span-4 -mt-2">
        <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
        <subscription-dialog :show-dialog="showSubscriptionDialog" @close-dialog="showSubscriptionDialog = false" :user-type="userType"
                             :subscription="subscription" :type="subEditType" @subs-added="addNewSubsToList"/>
        <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
          <div class="text-info-3 font-bold text-lg">{{ local.subscriptions }}</div>
          <button
              @click="()=>{subscription = {};subEditType = 'new' ;showSubscriptionDialog = true}"
              class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
            <i class="pi pi-plus text-sm mx-1"/>
            {{ local.add }} {{ local.subscription }}
          </button>
        </div>
        <subscriptions-list :subscriptions="subscriptions" @open-renew-subscription-dialog="openRenewSubscriptionDialog"
                            user-type="Customer"
                            @open-link-dialog="openLinkDialog" :is-loading="loading"
                            @open-renew-history-dialog="(payload) => {subscription = payload;showRenewHistoryDialog = true}"/>
        <div class="flex mt-3" v-if="!loading">
          <div class="flex" v-for="i in pages">
            <div class="text-lg"
                 v-if="(pages > 5) && ((i === onboarding-1 && i > 2) || (i === onboarding+2 && i !== pages))">...
            </div>
            <div
                v-if="pages <= 5 || (i === 1 || i === pages || i-1 === onboarding || i+1 === onboarding || i === onboarding)"
                class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
                :class="{'bg-opacity-50' : onboarding === i}"
                @click="onboarding = i">{{ i }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import router from "../../router/index.js";
import {useLocalization} from "../../store/localizationStore.js";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useDataStore} from "../../store/dataStore.js";
import SubscriptionsList from "../../components/subscriptionsList.vue";
import SubscriptionDialog from "../../components/subscriptionDialog.vue";
import SubscriptionLinkDialog from "../../components/subscriptionLinkDialog.vue";
import SubLookupDialog from "../../components/subLookupDialog.vue";
import axios from "axios";
import ChangePasswordDialog from "../../components/changePasswordDialog.vue";
import SubscriptionRenewHistoryDialog from "../../components/subscriptionRenewHistoryDialog.vue";

let isDark = computed(() => {
  return useDataStore().getDarkStatus
})

const changeThemeStatus = () => {
  showSettingMenu.value = false
  useDataStore().changeDarkStatus()
  document.cookie = `isDark=${useDataStore().getDarkStatus}`
}

let showLangMenu = ref(false)
let changeLanguage = (payload) => {
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
  document.cookie = `flag=${payload[0]}`
  document.cookie = `language=${payload[1]}`
  document.cookie = `direction=${payload[2]}`
}

let local = computed(() => useLocalization().getLocal)

let isRtl = computed(() => useLocalization().getDirection === 'rtl')

let lookupSubscription = reactive({
  id: undefined,
  title: undefined,
  startDate: undefined,
  expireDate: undefined,
  totalUsed: undefined,
  totalFlow: undefined,
  status: undefined
})

let subscriptions = ref([])

let subLink = ref('')
let showSubDetail = ref(false)
let showRenewHistoryDialog = ref(false)
let showDeactivateSubscriptionTag = ref(false)
let showActivateSubscriptionTag = ref(false)

const changeStatus = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/subscriptions/change-status?id=${lookupSubscription.id}&newStatus=${payload}`,
      {},
      {
        headers: {
          authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    subscription.status = payload
  }).catch((error) => {
    console.log(error)
  })
}

const searchSubscription = () => {
  if (subLink.value) {
    axios.get(`${useDataStore().getServerAddress}/subscriptions/report?subLink=${subLink.value}`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      showSubDetail.value = false
      lookupSubscription = response.data
      showSubDetail.value = true
    }).catch((error) => console.log(error))
  }
}

let subscription = reactive(undefined)
let link = ref('')
let subEditType = ref(false)
let pages = ref(1)
let onboarding = ref(1)
let showSubscriptionDialog = ref(false)
let showLinkDialog = ref(false)
const openRenewSubscriptionDialog = (payload) => {
  subscription = payload
  subEditType.value = 'ReNew'
  showSubscriptionDialog.value = true
}

const openLinkDialog = (payload) => {
  link.value = payload
  showLinkDialog.value = true
}

let showLookupTag = ref(false)
let showLookupDialog = ref(false)
let userType = ref("")

let loading = ref(true)

const getSubscriptions = () => {
  loading.value = true
  axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all?size=10&page=${onboarding.value - 1}&selfSubs=true`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    subscriptions.value = response.data.content
    loading.value = false
  })
}

let userDetail = reactive(
    {
      firstName : "",
      lastName : "",
      startDateTime : "",
      expirationDateTime : "",
      totalFlow : undefined,
      isIndefiniteFlow : undefined,
      debtAmount : undefined,
      totalUsed : undefined
    }
)

onMounted(() => {
  if (document.cookie) {
    let cookie = document.cookie.split('; ');
    let lang = ['', '', '']
    cookie.forEach((data) => {
      let value = data.split('=')
      if (value[0] === 'flag') {
        lang[0] = value[1]
      } else if (value[0] === 'language') {
        if (value[1].indexOf('[') !== -1) {
          lang = ['🇮🇷', 'fa', 'rtl']
          document.cookie = `flag=${lang[0]}`
          document.cookie = `language=${lang[1]}`
          document.cookie = `direction=${lang[2]}`
        } else {
          lang[1] = value[1]
        }
      } else if (value[0] === 'direction') {
        lang[2] = value[1]
      } else if (value[0] === 'isDark') {
        useDataStore().setDarkStatus(value[1] === 'true')
      } else if (value[0] === 'token') {
        useDataStore().setToken(value[1])
      }
    })
    useLocalization().changeLanguage(lang)
    axios.get(`${useDataStore().getServerAddress}/authentication/get-role`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      if (response.data !== 'Customer' && response.data !== 'SuperCustomer'){
        router.push('/')
      }
      else {
        userType.value = response.data
        getSubscriptions()
        getUserDetails()
      }
    }).catch((error) => {
      router.push('/')
    })
  } else router.push('/')
})
const getUserDetails = ()=>{
  axios.get(`${useDataStore().getServerAddress}/users/self-details`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    Object.assign(userDetail , response.data)
  }).catch((error) => {
    router.push('/')
  })
}

watch(() => onboarding.value, () => {
  subscriptions.value = []
  getSubscriptions()
})

const logOut = () => {
  document.cookie = `token=`
  router.push('/')
}

const addNewSubsToList = () => {
  getSubscriptions()
}

const showSettingMenu = ref(false)
const showChangePasswordDialog = ref(false)
</script>