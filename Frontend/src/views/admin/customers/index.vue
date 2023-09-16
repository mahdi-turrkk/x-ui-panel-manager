<template>
  <admin-layout>
    <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
    <customer-add-dialog :show-dialog="showCustomerAddDialog" @close-dialog="showCustomerAddDialog = false"/>
    <customer-edit-dialog :show-dialog="showCustomerEditDialog" @close-dialog="showCustomerEditDialog = false" :customer="customer"/>
    <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.customers }}</div>
      <button
          @click="()=>{showCustomerAddDialog = true}"
          class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
        <plus-icon class="w-4 h-4"/>
        {{ local.add }} {{ local.customer }}
      </button>
    </div>
    <customers-list @open-edit-Customer-dialog="openEditCustomerDialog" :customers="customers" @open-link-dialog="openLinkDialog"/>
    <div class="flex mt-6">
      <div
          class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
          v-for="i in pages" :class="{'bg-opacity-50' : onboarding === i}" @click="onboarding = i">{{ i }}
      </div>
    </div>
  </admin-layout>
</template>
<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {computed, onMounted, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import {PlusIcon} from "@heroicons/vue/24/solid/index.js";
import CustomersList from "../../../components/customersList.vue";
import CustomerAddDialog from "../../../components/customerAddDialog.vue";
import SubscriptionLinkDialog from "../../../components/subscriptionLinkDialog.vue";
import CustomerEditDialog from "../../../components/customerEditDialog.vue";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(10)
let onboarding = ref(1)

let showCustomerAddDialog = ref(false)
let showCustomerEditDialog = ref(false)
let showLinkDialog = ref(false)

let customer = reactive(undefined)
let link = ref('')

const openEditCustomerDialog = (payload) => {
  customer = payload
  showCustomerEditDialog.value = true
}
const openLinkDialog = (payload) => {
  link = payload
  showLinkDialog.value = true
}

const getCustomers = () => {
  axios.get(`${useDataStore().getServerAddress}/users/get-all?size=10&role=Customer&page=${onboarding.value-1}` ,
      {
        headers : {
          Authorization : useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    customers.value = response.data.content
  }).catch((error) => console.log(error))
}

let customers = ref([
  {
    customer: {id: '3522', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 1'
    }]
  },
  {
    customer: {id: '3523', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 2'
    }]
  },
  {
    customer: {id: '3524', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 3'
    }]
  },
  {
    customer: {id: '3525', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 4'
    }]
  },
  {
    customer: {id: '3526', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 5'
    }]
  },
  {
    customer: {id: '3527', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 6'
    }]
  },
  {
    customer: {id: '3528', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 7'
    }]
  },
  {
    customer: {id: '3529', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 8'
    }]
  },
  {
    customer: {id: '3530', username: 'husyn.cf', status: false , password : '45678c9dcjnsdfvb9wr%^' , firstName : 'hossein' , lastName : 'shakeri' , phoneNumber : '09141111111' , email : 'test@gixmetir.online' , address: 'iran,tabriz,pelake 2'},
    subscriptions: [{
      id: '35423',
      startDate: '2023/09/08',
      expireDate: '2023/09/08',
      totalUse: '419430400000',
      totalFlow: '545259520000',
      status: true,
      link: 'this is test link for qr generating 9'
    }]
  },
])

onMounted(()=>{
  getCustomers()
})
</script>