<template>
  <div class="w-full rounded-xl text-xs overflow-hidden my-2">
    <div>
      <div
          class="bg-background-3 flex rounded-t-xl justify-between items-center px-4 py-4 z-20 relative cursor-pointer text-info-3"
          :class="{'rounded-b-xl' : admin.id != onboarding}">
        <div class="flex w-full items-center space-x-2">
          <div class="w-[20%] no-scrollbar" @click="emits('setOnboarding' , admin.id)">{{ admin.id }}</div>
          <div class="w-[40%] no-scrollbar text-center" @click="emits('setOnboarding' , admin.id)">
            {{ admin.username }}
          </div>
          <div class="w-[20%] no-scrollbar flex justify-center">
            <div
                class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 text-success relative w-fit px-4"
                v-if="admin.enabled" @mouseenter="showDeactivateTag = true"
                @mouseleave="showDeactivateTag = false" @click="changeStatus(false)">{{ local.active }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-error"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateTag">{{ local.deactivate }}
                {{ local.admin }}
              </div>
            </div>
            <div
                class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 text-error relative w-fit px-4"
                v-if="!admin.enabled" @mouseenter="showActivateTag = true" @mouseleave="showActivateTag = false" @click="changeStatus(true)">
              {{ local.inactive }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-success"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateTag">{{ local.activate }}
                {{ local.admin }}
              </div>
            </div>
          </div>
          <div class="w-[20%] flex justify-center no-scrollbar relative" @mouseenter="showMenu = true" @mouseleave="showMenu = false" @click="showMenu = !showMenu">
            <div>
              <button class="flex justify-center items-center p-2">
                <i class="pi pi-cog text-lg md:text-xl mx-1 text-info-3"/>
              </button>
            </div>
            <div class="absolute flex w-fit bg-background-1 p-2 rounded-xl" v-if="showMenu">
              <div class="relative">
                <i class="pi pi-file-pdf text-lg md:text-xl mx-1 text-success" @mouseenter="showDownloadReportTag = true"
                   @mouseleave="showDownloadReportTag = false"
                   @click="downloadReport"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDownloadReportTag">
                  {{ local.downloadReport }}
                </div>
              </div>
              <div class="relative">
                <i class="pi pi-pencil text-lg md:text-xl mx-1 text-warning" @mouseenter="showEditTag = true"
                                    @mouseleave="showEditTag = false"
                                    @click="emits('openEditAdminDialog' , admin)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showEditTag">{{ local.edit }}
                  {{ local.admin }}
                </div>
              </div>
              <div class="relative">
                <i class="pi pi-lock text-lg md:text-xl mx-1 text-success" @mouseenter="showChangePasswordTag = true"
                                  @mouseleave="showChangePasswordTag = false"
                                  @click="emits('openChangePasswordDialog' , admin.id)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showChangePasswordTag">{{ local.changePassword }}
                </div>
              </div>
              <div class="relative">
                <i class="pi pi-trash text-lg md:text-xl mx-1 text-error" @mouseenter="showDeleteTag = true"
                                  @mouseleave="showDeleteTag = false"
                                  @click="emits('openDeleteConfirmationDialog' , admin)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeleteTag">{{ local.delete }} {{local.admin}}
                </div>
              </div>
            </div>
          </div>
        </div>
        <i class="pi pi-chevron-down text-base transition-all duration-300" @click="emits('setOnboarding' , admin.id)"
                           :class="{'rotate-180' : onboarding === admin.id}"/>
      </div>
      <div class="bg-background-2 px-8 md:px-12 py-4 transition-all duration-700 z-10 relative rounded-b-xl text-info-3"
           ref="expansionText"
           :style="{'margin-top' : marginTop}">
        <div class="text-sm font-bold">{{ local.subscriptions }} :</div>
        <div
            class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md mb-2 mt-4 font-bold text-sm"
            :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
          <div class="w-0 hidden md:inline-block md:w-[10%] text-xs md:text-sm">{{ local.id }}</div>
          <div class="hidden md:inline-block md:w-[25%] text-center text-xs md:text-sm">{{ local.plan }}</div>
          <div class="w-[30%] md:w-[20%] text-center text-xs md:text-sm">{{ local.remaining }}</div>
          <div class="w-[30%] md:w-[20%] text-center text-xs md:text-sm">{{ local.payStatus }}</div>
          <div class="w-[30%] md:w-[15%] text-center text-xs md:text-sm">{{ local.status }}</div>
          <div class="w-[10%] text-center text-xs md:text-sm">{{ local.actions }}</div>
        </div>
        <div class="h-full w-full flex justify-center items-center pt-12" v-if="!isLoading && subscriptions.length === 0">
          <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
            <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-2 px-2">{{ local.noRecords }}</div>
          </div>
        </div>
        <div class="flex justify-center mt-4" v-if="isLoading">
          <loader/>
        </div>
        <subscription-list-item v-for="subscription in subscriptions" :subscription="subscription" v-else-if="subscriptions.length > 0"
                                @open-renew-history-dialog="(payload) => {emits('openRenewHistoryDialog' , payload)}"
                                @open-link-dialog="openLinkDialog" @change-subscription-pay-status="(payload) => {subscription.markAsPaid = payload}"
                                @change-subscription-status="(payload) => {subscription.status = payload}" @open-delete-confirmation-dialog="(payload) => {emits('openDeleteSubConfirmationDialog' , payload)}"/>
        <div class="flex mt-3" v-if="!isLoading">
          <div class="flex" v-for="i in subsPages" >
            <div class="text-lg" v-if="(subsPages > 5) && ((i === onboardingSubsPage-1 && i > 2) || (i === onboardingSubsPage+2 && i !== subsPages))">...</div>
            <div
                v-if="subsPages <= 5 || (i === 1 || i === subsPages || i-1 === onboardingSubsPage || i+1 === onboardingSubsPage || i === onboardingSubsPage)"
                class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
                :class="{'bg-opacity-50' : onboardingSubsPage === i}"
                @click="onboardingSubsPage = i">{{ i }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {useDataStore} from "../store/dataStore.js";
import SubscriptionListItem from "./subscriptionListItem.vue";
import axios from "axios";
import Loader from "./loader.vue";

let props = defineProps(['onboarding', 'admin'])
const emits = defineEmits(['setOnboarding', 'openEditAdminDialog', 'openLinkDialog', 'changeAdminStatus' , 'openChangePasswordDialog' , 'openDeleteSubConfirmationDialog' , 'openRenewHistoryDialog'])

const expansionText = ref(null)

let marginTop = computed(() => {
  if (expansionText.value != null) {
    if (props.onboarding !== props.admin.id)
      return `-${expansionText.value.offsetHeight}px`
    else return 0
  }
})

let local = computed(() => {
  return useLocalization().getLocal
})

let showEditTag = ref(false)
let showActivateTag = ref(false)
let showDeactivateTag = ref(false)
let showMenu = ref(false)
let showDeleteTag = ref(false)
let showDownloadReportTag = ref(false)

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

const openLinkDialog = (payload) => {
  emits('openLinkDialog', payload)
}

let subscriptions = ref([])
let onboardingSubsPage = ref(1)
let subsPages = ref(1)
let isLoading = ref(true)
let isChangeInProgress = ref(false)


onMounted(() => {
  axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all?userId=${props.admin.id}&page=${onboardingSubsPage.value -1 }&size=10`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    subscriptions.value = response.data.content
    subsPages.value = response.data.totalPages
    emits('setOnboarding', props.admin.id)
    setTimeout(() => {
      emits('setOnboarding', undefined)
    }, 1)
    isLoading.value = false
  }).catch((error) => console.log(error))
})

const getSubscriptions = () => {
  isLoading.value = true
  axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all?userId=${props.admin.id}&page=${onboardingSubsPage.value -1 }&size=10`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    subscriptions.value = response.data.content
    subsPages.value = response.data.totalPages
    isLoading.value = false
  }).catch((error) => console.log(error))
}

watch(() => onboardingSubsPage.value , () => {
  getSubscriptions()
})
const changeStatus = (payload) => {
  if (!isChangeInProgress.value){
    isChangeInProgress.value = true
    axios.put(`${useDataStore().getServerAddress}/users/change-status?id=${props.admin.id}&newStatus=${payload}` ,
        {},
        {
          headers : {
            authorization : useDataStore().getToken
          }
        }
    ).then((response) => {
      emits('changeAdminStatus' , payload)
      isChangeInProgress.value = false
    }).catch((error) => {
      console.log(error)
      isChangeInProgress.value = false
    })
  }
}

let showChangePasswordTag = ref(false)

const downloadReport = () => {
  // Make an Axios GET request to download the file
  axios.get(`${useDataStore().getServerAddress}/users/download-user-report?userId=${props.admin.id}`, {
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
        downloadLink.download = 'report.csv'; // Set the desired file name
        // Trigger the download by clicking the link
        downloadLink.click();
      })
      .catch((error) => {
        console.error('Error downloading the file:', error);
      });
}
</script>


<style>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>