<template>
  <admin-layout>
    <server-dialog :show-dialog="showServerDialog" @close-dialog="showServerDialog = false" :server="server"/>
    <div class="w-full absolute top-5">
      <div class="w-fit bg-error text-white flex rounded-xl px-2 py-2 mx-auto items-center" v-if="showErrorMessage">
        <x-circle-icon class="w-5 h-5"/>
        <div>{{ local.inboundsLoadFailed }}</div>
      </div>
      <div class="w-fit bg-success text-white flex rounded-xl px-2 py-2 mx-auto items-center" v-if="showSuccessMessage">
        <check-circle-icon class="w-5 h-5"/>
        <div>{{ local.inboundsLoadSuccessfully }}</div>
      </div>
    </div>
    <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.servers }}</div>
      <div class="flex">
        <button
            @click="updateInbounds"
            class="mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
          <arrow-path-icon class="w-4 h-4"/>
          {{ local.update }} {{ local.inbounds }}
        </button>
        <button
            @click="()=>{server = {};showServerDialog = true}"
            class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
          <plus-icon class="w-4 h-4"/>
          {{ local.add }} {{ local.server }}
        </button>
      </div>
    </div>
    <servers-list @open-edit-server-dialog="openEditServerDialog" :servers="servers"/>
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
import ServersList from "../../../components/serversList.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import {PlusIcon, ArrowPathIcon, CheckCircleIcon, XCircleIcon} from "@heroicons/vue/24/solid/index.js";
import ServerDialog from "../../../components/serverDialog.vue";
import {useDataStore} from "../../../store/dataStore.js";
import axios from "axios";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(10)
let onboarding = ref(1)

let showServerDialog = ref(false)

let server = reactive(undefined)

const openEditServerDialog = (payload) => {
  server = payload
  showServerDialog.value = true
}

let servers = ref([
  {
    id: '15224',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '152247',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '15228',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '152249',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '15210',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '152211',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '1512',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
  {
    id: '152213',
    url: 'https://euro-1.gixmetir.online:8090',
    username: 'husyn.cf',
    password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
    generatable: false
  },
])

const loadServers = () => {
  axios.get(`${useDataStore().getServerAddress}/servers/get-all?size=10&page=${onboarding.value - 1}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    pages.value = response.data.totalPages
    servers.value = response.data.content
  }).catch((error) => console.log(error))
}

onMounted(() => {
  loadServers()
})

watch(() => onboarding.value, () => {
  loadServers()
})

let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)

const updateInbounds = () => {
  axios.get(`${useDataStore().getServerAddress}/inbounds/load-all-inbounds-from-x-ui-panel`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    loadServers()
    showSuccessMessage.value = true
    setTimeout(()=>{
      showSuccessMessage.value = false
    } , 2000)
  }).catch((error) => {
    console.log(error)
    showErrorMessage.value = true
    setTimeout(()=>{
      showErrorMessage.value = false
    } , 2000)
  })
}
</script>