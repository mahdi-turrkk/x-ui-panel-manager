<template>
  <admin-layout>
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
  </admin-layout>
</template>

<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {PlusIcon} from "@heroicons/vue/24/outline";
import SubscriptionsList from "../../../components/subscriptionsList.vue";
import {computed, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import SubscriptionDialog from "../../../components/subscriptionDialog.vue";
import SubscriptionLinkDialog from "../../../components/subscriptionLinkDialog.vue";

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
</script>
