<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="fixed top-4 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
      <i class="pi pi-times-circle text-xl"/>
      <div>{{ errorMessage }}</div>
    </div>
    <div class="fixed top-4 bg-success rounded-xl px-2 py-2 text-white flex" v-if="showSuccessMessage">
      <i class="pi pi-check-circle text-xl"/>
      <div>{{ local.serverSavedSuccessfully }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:min-w-fit rounded-xl flex flex-col py-4 bottom-0 max-h-[500px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.server }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <label class="z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
               :class="{'mt-[1px] opacity-100' : serverUrl}">{{ local.serverUrl }}</label>
        <input type="text" :placeholder="local.serverUrl" v-model="serverUrl"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
               :class="{'mt-[1px] opacity-100' : serverUsername}">{{ local.username }}</label>
        <input type="text" :placeholder="local.username" v-model="serverUsername"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
        <label class="z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
               :class="{'mt-[1px] opacity-100' : serverPassword}">{{ local.password }}</label>
        <input type="text" :placeholder="local.password" v-model="serverPassword"
               class="z-20 w-full md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      </div>
      <div class="flex justify-end mt-2">
        <button
            @click="emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="saveServer">
          {{ local.save }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";
import {displayHelper} from "../helpers/displayHelper.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

let serverId = ref(undefined)
let serverUrl = ref('')
let serverUsername = ref('')
let serverPassword = ref('')
let type = ref('add')
const props = defineProps(['showDialog', 'server'])

watch(() => props.server, (newVal) => {
  serverId.value = props.server.id
  serverUrl.value = props.server.url
  serverUsername.value = props.server.username
  serverPassword.value = props.server.password
})

const emits = defineEmits(['closeDialog', 'serverSubmitted'])

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}

let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)
let errorMessage = ref('')

const saveServer = () => {
  if (serverUrl.value && serverUsername.value && serverPassword.value) {
    if (serverId.value) {
      axios.put(`${useDataStore().getServerAddress}/servers/update?id=${serverId.value}`,
          {
            url: serverUrl.value,
            username: serverUsername.value,
            password: serverPassword.value,
            generatable: props.server.generatable
          },
          {
            headers: {
              Authorization: useDataStore().getToken
            }
          }
      ).then((response) => {
        showSuccessMessage.value = true
        setTimeout(() => {
          showSuccessMessage.value = false
        }, 1000)
        emits('closeDialog')
        emits('serverSubmitted')
      }).catch((error) => {
        errorMessage.value = useLocalization().errorSavingServer
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      })
    } else {
      axios.post(`${useDataStore().getServerAddress}/servers/create`,
          {
            url: serverUrl.value,
            username: serverUsername.value,
            password: serverPassword.value,
            generatable: true
          },
          {
            headers: {
              Authorization: useDataStore().getToken
            }
          }
      ).then((response) => {
        showSuccessMessage.value = true
        setTimeout(() => {
          showSuccessMessage.value = false
        }, 1000)
        emits('closeDialog')
        emits('serverSubmitted')
      }).catch((error) => {
        errorMessage.value = useLocalization().errorSavingServer
        showErrorMessage.value = true
        setTimeout(() => {
          showErrorMessage.value = false
        }, 2000)
      })
    }
  } else {
    errorMessage.value = useLocalization().errorFieldsOfServer
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  }
}
</script>


