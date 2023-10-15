<template>
  <admin-layout>
    <subscription-renew-history-dialog :show-dialog="showRenewHistoryDialog" @close-dialog="showRenewHistoryDialog = false" :subscription="subscription"/>
    <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
    <subscription-dialog :show-dialog="showSubscriptionDialog" @close-dialog="showSubscriptionDialog = false"
                         :subscription="subscription" :type="subDialogType" @subs-added="addNewSubsToList"/>
    <delete-confirmation-dialog :show-dialog="showDeleteConfirmationDialog" title="subscriptions" :data="deleteSubscription"
                                @close-dialog="showDeleteConfirmationDialog = false" @delete-complete="getSubscriptions"/>
    <div class=" rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.subscriptions }}</div>
      <button
          @click="()=>{subscription = {};subDialogType = 'new' ;showSubscriptionDialog = true}"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <i class="pi pi-plus text-sm mx-1"/>
        {{ local.add }} {{ local.subscription }}
      </button>
    </div>
    <subscriptions-list :subscriptions="subscriptions" @open-renew-subscription-dialog="openRenewSubscriptionDialog" @open-delete-confirmation-dialog="openDeleteConfirmationDialog"
                        @open-link-dialog="openLinkDialog" :is-loading="loading" @open-renew-history-dialog="(payload) => {subscription = payload;showRenewHistoryDialog = true}"/>
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
import SubscriptionsList from "../../../components/subscriptionsList.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import SubscriptionDialog from "../../../components/subscriptionDialog.vue";
import SubscriptionLinkDialog from "../../../components/subscriptionLinkDialog.vue";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";
import DeleteConfirmationDialog from "../../../components/deleteConfirmationDialog.vue";
import SubscriptionRenewHistoryDialog from "../../../components/subscriptionRenewHistoryDialog.vue";

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

const addNewSubsToList = () => {
  getSubscriptions()
}

let deleteSubscription = reactive({})
let showDeleteConfirmationDialog = ref(false)
const openDeleteConfirmationDialog = (payload) => {
  deleteSubscription = payload
  showDeleteConfirmationDialog.value = true
}

let showRenewHistoryDialog = ref(false)

</script>
