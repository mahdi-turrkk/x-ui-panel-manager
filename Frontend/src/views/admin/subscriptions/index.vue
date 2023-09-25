<template>
  <admin-layout>
    <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
    <subscription-dialog :show-dialog="showSubscriptionDialog" @close-dialog="showSubscriptionDialog = false"
                         :subscription="subscription" :type="subDialogType"/>
    <div class=" rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.subscriptions }}</div>
      <button
          @click="()=>{subscription = {};subDialogType = 'new' ;showSubscriptionDialog = true}"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <plus-icon class="w-4 h-4"/>
        {{ local.add }} {{ local.subscription }}
      </button>
    </div>
    <subscriptions-list :subscriptions="subscriptions" @open-renew-subscription-dialog="openRenewSubscriptionDialog"
                        @open-link-dialog="openLinkDialog" :is-loading="loading"/>
    <div class="flex mt-6" v-if="!loading">
      <div
          class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
          v-for="i in pages" :class="{'bg-opacity-50' : onboarding == i}" @click="onboarding = i">{{ i }}
      </div>
    </div>
  </admin-layout>
</template>

<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {PlusIcon} from "@heroicons/vue/24/outline";
import SubscriptionsList from "../../../components/subscriptionsList.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import SubscriptionDialog from "../../../components/subscriptionDialog.vue";
import SubscriptionLinkDialog from "../../../components/subscriptionLinkDialog.vue";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";

let subscriptions = ref([])

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(10)
let onboarding = ref(1)

let showSubscriptionDialog = ref(false)
let showLinkDialog = ref(false)

let subscription = reactive(undefined)
let link = ref('')
let isRenew = ref(false)

const openRenewSubscriptionDialog = (payload) => {
  subscription = payload
  isRenew.value = true
  showSubscriptionDialog.value = true
}

const openLinkDialog = (payload) => {
  link.value = payload
  showLinkDialog.value = true
}

let subDialogType = ref('')

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
  }).catch((error) => console.log(error))
}

onMounted(() => {
  getSubscriptions()
})

watch(() => onboarding.value , () => {
  subscriptions.value = []
  getSubscriptions()
})
</script>
