<template>
  <admin-layout>
    <server-dialog :show-dialog="showServerDialog" @close-dialog="showServerDialog = false" :server="server"/>
    <div class=" rounded-xl w-full py-3 px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.servers }}</div>
      <button
          @click="()=>{server = {};showServerDialog = true}"
          class="outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-6 py-2 flex space-x-1 items-center text-sm">
        <plus-icon class="w-4 h-4"/>
        {{ local.add }} {{ local.server }}
      </button>
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
import {computed, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import {PlusIcon} from "@heroicons/vue/24/solid/index.js";
import ServerDialog from "../../../components/serverDialog.vue";

let local = computed(() => {
  return useLocalization().getLocal
})
let pages = ref(10)
let onboarding = ref(1)

let showServerDialog = ref(false)

let server = reactive(undefined)

const openEditServerDialog = (payload)=>{
  server = payload
  showServerDialog.value = true
}

let servers = ref([
  {
    server: {
      id: '15224',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152247',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152248',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152249',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152250',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152251',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152252',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152253',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
  {
    server: {
      id: '152254',
      serverUrl: 'https://euro-1.gixmetir.online:8090',
      username: 'husyn.cf',
      password: '2CsJ7VDd?UtAGm^~!:FDMcd!A^1*epLDP*',
      generatable: false
    },
    inbounds: [{id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}, {
      id: '2434523',
      title: 'euro-2-50GB',
      port: '40231',
      status: true,
      generatable: false
    }, {id: '2434523', title: 'euro-2-50GB', port: '40231', status: true, generatable: false}]
  },
])
</script>