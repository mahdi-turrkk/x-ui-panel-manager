<template>
  <div class="w-full rounded-xl text-xs overflow-hidden my-2">
    <div>
      <div
          class="bg-background-3 flex rounded-t-xl justify-between items-center px-4 py-4 z-20 relative cursor-pointer text-info-3"
          :class="{'rounded-b-xl' : customer.id != onboarding}">
        <div class="flex w-full items-center space-x-2">
          <div class="w-0 hidden md:w-[10%] md:inline-block no-scrollbar" @click="emits('setOnboarding' , customer.id)">{{ customer.id }}</div>
          <div class="w-[30%] md:w-[20%] no-scrollbar text-center" @click="emits('setOnboarding' , customer.id)">{{ customer.totalFlow == 0 ? "∞" : customer.totalFlow - customer.totalUsed + 'GB' }} - {{ customer.expirationDateTime ?  customer.expirationDateTime.substring(0,10) : "∞" }}</div>
          <div class="w-[30%] no-scrollbar text-center overflow-auto" @click="emits('setOnboarding' , customer.id)">
            {{ customer.username }}
          </div>
          <div class="w-[20%] no-scrollbar flex justify-center">
            <div
                class="bg-success bg-opacity-20 border-success border-2 rounded-xl text-center py-1 text-success relative w-fit px-4"
                v-if="customer.enabled" @mouseenter="showDeactivateTag = true"
                @mouseleave="showDeactivateTag = false" @click="changeStatus(false)">{{ local.active }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-error"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeactivateTag">{{ local.deactivate }}
                {{ local.customer }}
              </div>
            </div>
            <div
                class="bg-error bg-opacity-20 border-error border-2 rounded-xl text-center py-1 text-error relative w-fit px-4"
                v-if="!customer.enabled" @mouseenter="showActivateTag = true" @mouseleave="showActivateTag = false" @click="changeStatus(true)">
              {{ local.inactive }}
              <div class="absolute -top-4 bg-background-3 w-max rounded-xl px-2 py-1 text-success"
                   :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showActivateTag">{{ local.activate }}
                {{ local.customer }}
              </div>
            </div>
          </div>
          <div class="w-[20%] flex justify-center no-scrollbar relative" @mouseenter="showMenu = true" @mouseleave="showMenu = false" @click="showMenu = !showMenu">
            <div>
              <button class="flex justify-center items-center p-2">
                <cog8-tooth-icon class="w-6 h-6 text-info-3"/>
              </button>
            </div>
            <div class="absolute flex w-fit bg-background-1 p-2 rounded-xl" v-if="showMenu">
              <div class="relative">
                <pencil-square-icon class="w-6 h-6 text-warning mx-1" @mouseenter="showEditTag = true"
                                    @mouseleave="showEditTag = false"
                                    @click="emits('openEditCustomerDialog' , customer)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showEditTag">{{ local.edit }}
                  {{ local.customer }}
                </div>
              </div>
              <div class="relative">
                <lock-closed-icon class="w-6 h-6 text-success mx-1" @mouseenter="showChangePasswordTag = true"
                                  @mouseleave="showChangePasswordTag = false"
                                  @click="emits('openChangePasswordDialog' , customer.id)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showChangePasswordTag">{{ local.changePassword }}
                </div>
              </div>
              <div class="relative">
                <trash-icon class="w-6 h-6 text-error mx-1" @mouseenter="showDeleteTag = true"
                            @mouseleave="showDeleteTag = false"
                            @click="emits('openDeleteConfirmationDialog' , customer)"/>
                <div class="absolute -top-6 bg-background-3 opacity-70 w-max rounded-xl px-2 py-1"
                     :class="{'-left-8' : !isRtl , '-right-8' : isRtl}" v-if="showDeleteTag">{{ local.delete }} {{local.customer}}
                </div>
              </div>
            </div>
          </div>
        </div>
        <chevron-down-icon class="h-4 w-4 md:h-5 md:w-5 transition-all duration-300" @click="emits('setOnboarding' , customer.id)"
                           :class="{'rotate-180' : onboarding === customer.id}"/>
      </div>
      <div class="bg-background-2 px-8 md:px-12 py-4 transition-all duration-700 z-10 relative rounded-b-xl text-info-3"
           ref="expansionText"
           :style="{'margin-top' : marginTop}">
        <div class="text-sm font-bold">{{ local.subscriptions }} :</div>
        <div
            class="flex space-x-2 items-center px-4 py-4 w-full bg-background-3 rounded-xl shadow-md mb-2 mt-4 font-bold text-sm"
            :class="{'shadow-info-1 shadow-sm' : useDataStore().getDarkStatus}">
          <div class="w-0 hidden md:inline-block md:w-[10%] text-xs md:text-sm">{{ local.id }}</div>
          <div class="w-[30%] text-center text-xs md:text-sm">{{ local.startEndDate }}</div>
          <div class="w-[30%] text-center text-xs md:text-sm">{{ local.usage }}</div>
          <div class="w-[30%] md:w-[20%] text-center text-xs md:text-sm">{{ local.status }}</div>
          <div class="w-[10%] text-center text-xs md:text-sm">{{ local.url }}</div>
        </div>
        <div class="h-full w-full flex justify-center items-center pt-12" v-if="subscriptions.length === 0">
          <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
            <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-2 px-2">{{ local.noRecords }}</div>
          </div>
        </div>
        <subscription-list-item v-for="subscription in subscriptions" :subscription="subscription" v-if="subscriptions.length > 0"
                                @open-link-dialog="openLinkDialog" @open-delete-confirmation-dialog="openDeleteConfirmationDialogSubscription"
                                @change-subscription-status="(payload) => {subscription.status = payload}"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ChevronDownIcon, ArrowPathIcon, PencilSquareIcon, PlusIcon , LockClosedIcon} from "@heroicons/vue/24/outline";
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {useDataStore} from "../store/dataStore.js";
import SubscriptionListItem from "./subscriptionListItem.vue";
import {Cog8ToothIcon, QrCodeIcon, TrashIcon} from "@heroicons/vue/24/outline/index.js";
import axios from "axios";

let props = defineProps(['onboarding', 'customer'])
const emits = defineEmits(['setOnboarding', 'openEditCustomerDialog', 'openLinkDialog', 'changeCustomerStatus' , 'openChangePasswordDialog' , 'openDeleteConfirmationDialog' , 'openDeleteConfirmationDialogSubscription'])

const expansionText = ref(null)

let marginTop = computed(() => {
  if (expansionText.value != null) {
    if (props.onboarding !== props.customer.id)
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

let isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

const openLinkDialog = (payload) => {
  emits('openLinkDialog', payload)
}

let subscriptions = ref([])

onMounted(() => {
  axios.get(`${useDataStore().getServerAddress}/subscriptions/get-all?userId=${props.customer.id}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      }
  ).then((response) => {
    subscriptions.value = response.data.content
    emits('setOnboarding', props.customer.id)
    setTimeout(() => {
      emits('setOnboarding', undefined)
    }, 1)
  }).catch((error) => console.log(error))
})

const changeStatus = (payload) => {
  axios.put(`${useDataStore().getServerAddress}/users/change-status?id=${props.customer.id}&newStatus=${payload}` ,
      {},
      {
        headers : {
          authorization : useDataStore().getToken
        }
      }
  ).then((response) => {
    emits('changeCustomerStatus' , payload)
  }).catch((error) => {console.log(error)})
}

let showChangePasswordTag = ref(false)

let showMenu = ref(false)
let showDeleteTag = ref(false)

const openDeleteConfirmationDialogSubscription = (payload) => {
  emits('openDeleteConfirmationDialogSubscription'  ,payload)
}
</script>


<style>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>