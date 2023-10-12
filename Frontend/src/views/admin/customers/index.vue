<template>
  <admin-layout>
    <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
    <customer-add-dialog :show-dialog="showCustomerAddDialog" @close-dialog="showCustomerAddDialog = false"
                         @user-added="getCustomers"/>
    <customer-edit-dialog :show-dialog="showCustomerEditDialog" @close-dialog="showCustomerEditDialog = false"
                          :customer="customer" @customer-edited="getCustomers"/>
    <change-password-dialog :show-dialog="showChangePasswordDialog" @close-dialog="showChangePasswordDialog = false"
                            :isSelf="false" :user-id="passwordEditUserId"/>
    <delete-confirmation-dialog :show-dialog="showDeleteConfirmationDialog" :title="deleteConfirmationType" :data="deleteObj"
                                @close-dialog="showDeleteConfirmationDialog = false" @delete-complete="getCustomers"/>
    <div class=" rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.customers }}</div>
      <button
          @click="()=>{showCustomerAddDialog = true}"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <i class="pi pi-plus text-sm mx-1"/>
        {{ local.add }} {{ local.customer }}
      </button>
    </div>
    <customers-list @open-edit-Customer-dialog="openEditCustomerDialog" :customers="customers"
                    @open-link-dialog="openLinkDialog" :is-loading="loading"
                    @open-change-password-dialog="openChangePasswordDialog"
                    @open-delete-confirmation-dialog-subscription="openDeleteConfirmationDialogSubscription"
                    @open-delete-confirmation-dialog="openDeleteConfirmationDialog"/>
    <div class="flex mt-6" v-if="!loading">
      <div
          class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
          v-for="i in pages" :class="{'bg-opacity-50' : onboarding === i}" @click="onboarding = i">{{ i }}
      </div>
    </div>
  </admin-layout>
</template>
<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import CustomersList from "../../../components/customersList.vue";
import CustomerAddDialog from "../../../components/customerAddDialog.vue";
import SubscriptionLinkDialog from "../../../components/subscriptionLinkDialog.vue";
import CustomerEditDialog from "../../../components/customerEditDialog.vue";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";
import ChangePasswordDialog from "../../../components/changePasswordDialog.vue";
import DeleteConfirmationDialog from "../../../components/deleteConfirmationDialog.vue";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(1)
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

let customers = ref([])

let loading = ref(true)

const getCustomers = () => {
  loading.value = true
  axios.get(`${useDataStore().getServerAddress}/users/get-all?size=10&role=Customer&page=${onboarding.value - 1}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    customers.value = response.data.content
    loading.value = false
  }).catch((error) => console.log(error))
}


onMounted(() => {
  getCustomers()
})

watch(() => onboarding.value, () => {
  customers.value = []
  getCustomers()
})

let passwordEditUserId = ref('')
const showChangePasswordDialog = ref(false)

const openChangePasswordDialog = (payload) => {
  passwordEditUserId.value = payload
  showChangePasswordDialog.value = true
}

let deleteObj = reactive({})
let showDeleteConfirmationDialog = ref(false)
let deleteConfirmationType = ref('')
const openDeleteConfirmationDialog = (payload) => {
  deleteObj = payload
  deleteConfirmationType.value = 'users'
  showDeleteConfirmationDialog.value = true
}

const openDeleteConfirmationDialogSubscription = (payload) => {
  deleteObj = payload
  deleteConfirmationType.value = 'subscriptions'
  showDeleteConfirmationDialog.value = true
}
</script>