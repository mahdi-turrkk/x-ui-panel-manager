<template>
  <admin-layout>
    <div class="w-full absolute top-3">
      <div class="w-fit bg-error text-white flex rounded-xl px-2 py-2 mx-auto" v-if="showErrorMessage">
        <i class="pi pi-times-circle text-xl"/>
        <div>{{ errorMessage }}</div>
      </div>
      <div class="w-fit bg-success text-white flex rounded-xl px-2 py-2 mx-auto" v-if="showSuccessMessage">
        <i class="pi pi-check-circle text-xl"/>
        <div>{{ successMessage }}</div>
      </div>
    </div>
    <div class="rounded-xl w-full pt-3 px-2 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg px-4">{{ local.dashboard }}</div>
      <button
          @click="syncWithPanel"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <i class="pi pi-sync text-sm mx-1"/>
        {{ local.syncWithPanel }}
      </button>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-2">
      <div
          class="mt-6 bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.subLookUp }}</div>
        <div class="flex relative w-full">
          <input type="text" :placeholder="local.subscriptionUrl" v-model="subLink"
                 class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
                 :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}" @keydown.enter="searchSubscription"
          />
          <i class="pi pi-search text-lg z-20 text-info-2 absolute top-3 cursor-pointer hover:text-info-1"
                                 :class="{'left-2' : isRtl , 'right-2' : !isRtl}"
                                 @click="searchSubscription"/>
        </div>
        <div class="font-bold text-center flex flex-col space-y-2 px-6 relative"
             :class="{'text-background-3 select-none' : !showSubDetail}">
          <div class="flex">{{ local.id }}&nbsp;:&nbsp;<div class="font-normal"
                                                            :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.id }}
          </div>
          </div>
          <div class="flex">{{ local.title }}&nbsp;:&nbsp;<div class="font-normal"
                                                               :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.title }}
          </div>
          </div>
          <div class="flex">{{ local.startDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.startDate ? subscription.startDate.substring(0, 10) : '' }}
          </div>
          </div>
          <div class="flex">{{ local.expireDate }}&nbsp;:&nbsp;<div class="font-normal"
                                                                    :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.expireDate ? subscription.expireDate.substring(0, 10) : '' }}
          </div>
          </div>
          <div class="flex">{{ local.totalUsed }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.totalUsed ? Number(subscription.totalUsed).toFixed(2) : '0.00' }}&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.totalFlow }}&nbsp;:&nbsp;<div class="font-normal" style="direction: ltr"
                                                                   :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
            {{ subscription.totalFlow }}&nbsp;GB
          </div>
          </div>
          <div class="flex">{{ local.status }}&nbsp;:&nbsp;<div class="font-normal"
                                                                :class="{'mr-auto' : isRtl , 'ml-auto': !isRtl}">
            {{ subscription.status ? local.active : local.inactive }}
          </div>
          </div>
          <div class="absolute top-0 bottom-0 right-0 left-0 text-info-2 px-6 md:px-10 pb-14"
               v-if="!showSubDetail">
            <i class="pi pi-file text-[60px] md:text-[100px] mt-4 mb-8 mx-auto"/>
            <div class="text-xs md:text-sm">{{ local.enterSubToSearch }}</div>
          </div>
        </div>
      </div>
      <div
          class="mt-6 bg-background-3 text-info-3 rounded-xl px-4 py-4 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.database }}</div>
        <div class="grid grid-cols-1 md:grid-cols-2 h-full py-4 px-4">
          <div @click="uploadDatabase"
               class="mx-2 my-2 rounded-xl py-4 border border-secondary-3 px-10 flex flex-col justify-center items-center text-secondary-3 brightness-75 hover:brightness-100 transition-all duration-200 cursor-pointer">
            <i class="pi pi-cloud-upload text-[80px] mt-4 mb-8 mx-auto"/>
            <div class="text-sm mt-4">{{ local.upload }} {{ local.database }}</div>
          </div>
          <div @click="downloadDatabase"
               class="mx-2 my-2 rounded-xl py-4 border border-primary-1 px-10 flex flex-col justify-center items-center text-primary-1 brightness-75 hover:brightness-150 transition-all duration-200 cursor-pointer">
            <i class="pi pi-cloud-download text-[80px] mt-4 mb-8 mx-auto"/>
            <div class="text-sm mt-4">{{ local.download }} {{ local.database }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="w-full mt-6">
      <div
          class="bg-background-3 text-info-3 rounded-xl px-4 py-4 pb-8 text-xs md:text-lg flex flex-col space-y-4 mx-2">
        <div class="font-bold text-sm md:text-xl text-center">{{ local.activitySummary }}</div>
        <div class="flex relative w-full">
          <div class="w-full grid grid-cols-1 md:grid-cols-2 mt-6">
            <div class="font-bold text-center flex flex-col space-y-4 px-6 relative">
              <div class="flex">{{ local.lastMonthSells }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                                  :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.lastMonthSells }}
              </div>
              </div>
              <div class="flex">{{ local.totalSells }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                              :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.totalSells }}
              </div>
              </div>
              <div class="flex">{{ local.totalUpload }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                               style="direction: ltr"
                                                                               :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.totalUpload ? data.totalUpload.toFixed(2) : '0.00' }}&nbsp;&nbsp;&nbsp;GB
              </div>
              </div>
              <div class="flex">{{ local.totalDownload }}&nbsp;&nbsp;:&nbsp;<div class="font-normal"
                                                                                 style="direction: ltr"
                                                                                 :class="{'mr-auto' : isRtl , 'ml-auto' : !isRtl}">
                {{ data.totalDownload ? data.totalDownload.toFixed(2) : '0.00' }}&nbsp;&nbsp;&nbsp;GB
              </div>
              </div>
            </div>
            <div class="w-56 mx-auto -mt-8 hidden md:inline-block text-center">
              <i class="pi pi-chart-line text-[150px] my-4 text-info-2"/>
            </div>
          </div>
        </div>
      </div>
    </div>
    <input type="file" accept=".db" class="hidden" ref="input" @change="uploadDb" :files="files">
  </admin-layout>
</template>
<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {computed, onMounted, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";


let local = computed(() => useLocalization().getLocal)
let isRtl = computed(() => useLocalization().getDirection === 'rtl')

let subLink = ref('')

let subscription = reactive({
  id: undefined,
  title: undefined,
  startDate: undefined,
  expireDate: undefined,
  totalUsed: undefined,
  totalFlow: undefined,
  status: true
})

let showSubDetail = ref(false)

let data = reactive({
  lastMonthSells: 0,
  totalSells: 0,
  totalUpload: 0,
  totalDownload: 0
})

const searchSubscription = () => {
  if (subLink.value) {
    axios.get(`${useDataStore().getServerAddress}/subscriptions/report?subLink=${subLink.value}`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      showSubDetail.value = false
      subscription = response.data
      showSubDetail.value = true
    }).catch((error) => console.log(error))
  }
}

const downloadDatabase = () => {
  const serverAddress = useDataStore().getServerAddress;
  const downloadUrl = `${serverAddress}/databases/download`;
  // Make an Axios GET request to download the file
  axios.get(downloadUrl, {
    headers: {
      Authorization: useDataStore().getToken
    },
    responseType: 'blob', // Important: set the response type to 'blob' to handle binary data
  })
      .then((response) => {
        // Create a blob object from the response data
        const blob = new Blob([response.data], {type: response.headers['content-type']});

        // Create a link element to trigger the download
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(blob);
        downloadLink.download = 'LocalDB.db'; // Set the desired file name

        // Trigger the download by clicking the link
        downloadLink.click();
      })
      .catch((error) => {
        console.error('Error downloading the file:', error);
      });
}

let input = ref(null)
let files = ref([])

const uploadDatabase = () => {
  input.value.click()
}

let showErrorMessage = ref(false)
let showSuccessMessage = ref(false)
let successMessage = ref('')
let errorMessage = ref('')

const uploadDb = (event) => {
  let dbFile = new FormData()
  dbFile.append('file', event.target.files[0], event.target.files[0].name)
  axios.post(`${useDataStore().getServerAddress}/databases/upload`,
      dbFile,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }).then((response) => {
    successMessage.value = local.value.databaseUploadSuccessful
    showSuccessMessage.value = true
    setTimeout(() => {
      showSuccessMessage.value = false
    }, 2000)
  }).catch((error) => {
    errorMessage.value = local.value.databaseUploadFailed
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 1000)
  })
}

const syncWithPanel = () => {
  axios.get(`${useDataStore().getServerAddress}/subscriptions/sync`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    successMessage.value = local.value.syncWithPanelSuccessful
    showSuccessMessage.value = true
    setTimeout(() => {
      showSuccessMessage.value = false
    }, 2000)
  }).catch((error) => {
    errorMessage.value = local.value.syncWithPanelFailed
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 1000)
  })
}

onMounted(() => {
  axios.get(`${useDataStore().getServerAddress}/subscriptions/summary`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    data.lastMonthSells = response.data.totalLastMonthSold
    data.totalSells = response.data.totalSold
    data.totalDownload = response.data.totalDownload
    data.totalUpload = response.data.totalUpload
  })
})


</script>