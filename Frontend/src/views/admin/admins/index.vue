<template>
  <admin-layout>
    <subscription-link-dialog @close-dialog="showLinkDialog = false" :show-dialog="showLinkDialog" :link="link"/>
    <admin-add-dialog :show-dialog="showAdminAddDialog" @close-dialog="showAdminAddDialog = false"
                      @user-added="getAdmins"/>
    <admin-edit-dialog :show-dialog="showAdminEditDialog" @close-dialog="showAdminEditDialog = false" :admin="admin"
                       @admin-edited="getAdmins"/>
    <change-password-dialog :show-dialog="showChangePasswordDialog" @close-dialog="showChangePasswordDialog = false"
                            :isSelf="false" :user-id="passwordEditUserId"/>
    <delete-confirmation-dialog :show-dialog="showDeleteConfirmationDialog" :title="deleteTitle" :data="deleteObj"
                                @close-dialog="showDeleteConfirmationDialog = false" @delete-complete="getAdmins"/>
    <subscription-renew-history-dialog  userType="Admin" :show-dialog="showRenewHistoryDialog" @close-dialog="showRenewHistoryDialog = false" :subscription="subscriptionHistory"/>
    <div class=" rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.admins }}</div>
      <button
          @click="()=>{showAdminAddDialog = true}"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <i class="pi pi-plus text-sm mx-1"/>
        {{ local.add }} {{ local.admin }}
      </button>
    </div>
    <admins-list @open-edit-admin-dialog="openEditAdminDialog" :admins="admins" @open-link-dialog="openLinkDialog"
                 @open-renew-history-dialog="(payload) => {subscriptionHistory = payload;showRenewHistoryDialog = true}"
                 :is-loading="loading" @open-change-password-dialog="openChangePasswordDialog"
                 @open-delete-confirmation-dialog="openDeleteConfirmationDialog" @open-delete-sub-confirmation-dialog="openDeleteSubConfirmationDialog"/>
    <div class="flex mt-3" v-if="!loading">
      <div class="flex" v-for="i in pages" >
        <div class="text-lg" v-if="(pages > 5) && ((i === onboarding-1 && i > 2) || (i === onboarding+2 && i !== pages))">...</div>
        <div
            v-if="pages <= 5 || (i === 1 || i === pages || i-1 === onboarding || i+1 === onboarding || i === onboarding)"
            class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
            :class="{'bg-opacity-50' : onboarding === i}"
            @click="onboarding = i">{{ i }}
        </div>
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
import AdminAddDialog from "../../../components/adminAddDialog.vue";
import AdminEditDialog from "../../../components/adminEditDialog.vue";
import AdminsList from "../../../components/adminsList.vue";
import ChangePasswordDialog from "../../../components/changePasswordDialog.vue";
import DeleteConfirmationDialog from "../../../components/deleteConfirmationDialog.vue";
import SubscriptionRenewHistoryDialog from "../../../components/subscriptionRenewHistoryDialog.vue";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(1)
let onboarding = ref(1)

let showAdminAddDialog = ref(false)
let showAdminEditDialog = ref(false)
let showLinkDialog = ref(false)
let showRenewHistoryDialog = ref(false)

let subscriptionHistory = reactive({})

let admin = reactive(undefined)
let link = ref('')

const openEditAdminDialog = (payload) => {
  admin = payload
  showAdminEditDialog.value = true
}
const openLinkDialog = (payload) => {
  link = payload
  showLinkDialog.value = true
}

let admins = ref([])

let loading = ref(true)

const getAdmins = () => {
  loading.value = true
  axios.get(`${useDataStore().getServerAddress}/users/get-all?size=10&role=Admin&page=${onboarding.value - 1}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    admins.value = response.data.content
    loading.value = false
  }).catch((error) => console.log(error))
}


onMounted(() => {
  getAdmins()
})

watch(() => onboarding.value, () => {
  admins.value = []
  getAdmins()
})

let passwordEditUserId = ref('')
const showChangePasswordDialog = ref(false)

const openChangePasswordDialog = (payload) => {
  passwordEditUserId.value = payload
  showChangePasswordDialog.value = true
}

let deleteObj = reactive({})
let showDeleteConfirmationDialog = ref(false)
let deleteTitle = ref('')
const openDeleteConfirmationDialog = (payload) => {
  deleteObj = payload
  deleteTitle.value = 'users'
  showDeleteConfirmationDialog.value = true
}

const openDeleteSubConfirmationDialog = (payload) => {
  deleteObj = payload
  deleteTitle.value = 'subscriptions'
  showDeleteConfirmationDialog.value = true
}
</script>