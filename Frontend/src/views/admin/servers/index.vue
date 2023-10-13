<template>
  <admin-layout>
    <server-dialog :show-dialog="showServerDialog" @close-dialog="showServerDialog = false" :server="server" @server-submitted="loadServers"/>
    <delete-confirmation-dialog :show-dialog="showDeleteConfirmationDialog" title="servers" :data="deleteServer"
                                @close-dialog="showDeleteConfirmationDialog = false" @delete-complete="loadServers"/>
    <div class="w-full absolute top-5">
      <div class="w-fit bg-error text-white flex rounded-xl px-2 py-2 mx-auto items-center" v-if="showErrorMessage">
        <i class="pi pi-times-circle text-xl"/>
        <div>{{ errorMessage }}</div>
      </div>
      <div class="w-fit bg-success text-white flex rounded-xl px-2 py-2 mx-auto items-center" v-if="showSuccessMessage">
        <i class="pi pi-check-circle text-xl"/>
        <div>{{ successMessage }}</div>
      </div>
    </div>
    <div class="rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.servers }}</div>
      <div class="flex">
        <button
            @click="updateSubscriptions"
            class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
          <i class="pi pi-sync text-sm mx-1"/>
          {{ displayHelper(windowWidth).smAndUp ? local.syncSubs : '' }}
        </button>
        <button
            @click="updateInbounds"
            class="text-xs md:text-sm outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
          <i class="pi pi-refresh text-sm mx-1"/>
          {{ displayHelper(windowWidth).smAndUp ? local.update : '' }}
          {{ displayHelper(windowWidth).lgAndUp ? local.inbounds : '' }}
        </button>
        <button
            @click="()=>{server = {};showServerDialog = true}"
            class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
          <i class="pi pi-plus text-sm mx-1"/>
          {{ displayHelper(windowWidth).smAndUp ? local.add : '' }}
          {{ displayHelper(windowWidth).lgAndUp ? local.server : '' }}
        </button>
      </div>
    </div>
    <servers-list @open-edit-server-dialog="openEditServerDialog" :servers="servers" :is-loading="loading" @open-delete-confirmation-dialog="openDeleteConfirmationDialog"/>
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
import ServersList from "../../../components/serversList.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import ServerDialog from "../../../components/serverDialog.vue";
import {useDataStore} from "../../../store/dataStore.js";
import axios from "axios";
import Loader from "../../../components/loader.vue";
import {displayHelper} from "../../../helpers/displayHelper.js";
import DeleteConfirmationDialog from "../../../components/deleteConfirmationDialog.vue";

let local = computed(() => {
  return useLocalization().getLocal
})

let loading = ref(true)

let pages = ref(1)
let onboarding = ref(1)

let showServerDialog = ref(false)

let server = reactive(undefined)

const openEditServerDialog = (payload) => {
  server = payload
  showServerDialog.value = true
}

let servers = ref([])

const loadServers = () => {
  loading.value = true
  axios.get(`${useDataStore().getServerAddress}/servers/get-all?size=10&page=${onboarding.value - 1}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    servers.value = response.data.content
    loading.value = false
  }).catch((error) => console.log(error))
}

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
  loadServers()
})

watch(() => onboarding.value, () => {
  loadServers()
})

let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)
let errorMessage = ref('')
let successMessage = ref('')

const updateInbounds = () => {
  axios.get(`${useDataStore().getServerAddress}/inbounds/load-all-inbounds-from-x-ui-panels`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    loadServers()
    successMessage.value = local.value.inboundsLoadSuccessfully
    showSuccessMessage.value = true
    setTimeout(() => {
      showSuccessMessage.value = false
    }, 2000)
  }).catch((error) => {
    console.log(error)
    errorMessage.value = local.value.inboundsLoadFailed
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  })
}

const updateSubscriptions = () => {
  axios.get(`${useDataStore().getServerAddress}/servers/sync-subscriptions-with-servers`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    successMessage.value = local.value.subsSyncedSuccessfully
    showSuccessMessage.value = true
    setTimeout(() => {
      showSuccessMessage.value = false
    }, 2000)
  }).catch((error) => {
    console.log(error)
    errorMessage.value = local.value.syncSubsFailed
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  })
}

let deleteServer = reactive({})
let showDeleteConfirmationDialog = ref(false)
const openDeleteConfirmationDialog = (payload) => {
  deleteServer = payload
  showDeleteConfirmationDialog.value = true
}
</script>