<template>
  <admin-layout>
    <customer-dialog :show-dialog="showCustomerDialog" @close-dialog="showCustomerDialog = false" :customer="customer"/>
    <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.customers }}</div>
      <button
          @click="()=>{customer = {};showCustomerDialog = true}"
          class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
        <plus-icon class="w-4 h-4"/>
        {{ local.add }} {{ local.customer }}
      </button>
    </div>
    <customers-list @open-edit-Customer-dialog="openEditCustomerDialog" :customers="customers"/>
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
import {computed, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import {PlusIcon} from "@heroicons/vue/24/solid/index.js";
import CustomersList from "../../../components/customersList.vue";
import CustomerDialog from "../../../components/customerDialog.vue";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(10)
let onboarding = ref(1)

let showCustomerDialog = ref(false)

let customer = reactive(undefined)

const openEditCustomerDialog = (payload) => {
  customer = payload
  showCustomerDialog.value = true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
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
      status: true
    }]
  },
])
</script>