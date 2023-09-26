import {defineStore} from 'pinia'

export const useLocalization = defineStore('localize', {
    state: () => {
        return {
            language: 'fa',
            flag: '🇮🇷',
            direction: 'rtl',
            locals: {
                fa: {
                    username: 'نام‌کاربری',
                    password: 'رمز‌عبور',
                    firstName: 'نام',
                    lastName: 'نام‌خانوادگی',
                    email: 'ایمیل',
                    phoneNumber: 'شماره تماس',
                    address: 'آدرس',
                    signIn: 'ورود',
                    signOut: 'خروج',
                    dashboard: 'داشبورد',
                    servers: 'سرورها',
                    customers: 'مشتری‌ها',
                    customer: 'مشتری‌',
                    subscriptions: 'اشتراک‌ها',
                    subscription: 'اشتراک‌',
                    new: 'جدید',
                    edit: 'ویرایش',
                    delete: 'حذف',
                    update: 'بروزرسانی',
                    add: 'افزودن',
                    id: 'شناسه',
                    serverUrl: 'آدرس سرور',
                    server: 'سرور',
                    information: 'اطلاعات',
                    generatable: 'قابلیت تولید',
                    actions: 'عملیات',
                    active: 'فعال',
                    activate: 'فعالسازی',
                    deactivate: 'غیرفعالسازی',
                    inactive: 'غیرفعال',
                    inbounds: 'دسته کانفیگ‌ها',
                    inbound: 'دسته کانفیگ',
                    title: 'عنوان',
                    port: 'پورت',
                    status: 'وضعیت',
                    save: 'ذخیره',
                    cancel: 'لغو',
                    show: 'نمایش',
                    url: 'لینک',
                    startEndDate: 'تاریخ شروع/پایان',
                    usage: 'مصرف',
                    renew: 'تمدید',
                    totalFlow : 'حجم کل',
                    ipLimit: 'تعداد کاربر',
                    periodLength: 'طول دوره (روز)',
                    clickToCopy: 'برای کپی روی بارکد یا لینک کلیک کنید.',
                    subscriptionUrl : 'لینک اشتراک',
                    copySuccessful : 'لینک کپی شد',
                    copyUnsuccessful : 'خطایی رخ داده',
                    subLookUp : 'استعلام وضعیت اشتراک',
                    startDate: 'تاریخ شروع',
                    expireDate: 'تاریخ انقضا',
                    totalUsed: 'حجم مصرف‌شده',
                    enterSubToSearch : 'برای گزارش‌گیری لینک اشتراک را وارد کرده و روی ذره‌بین کلیک کنید.',
                    database: 'پایگاه داده',
                    upload: 'آپلود',
                    download: 'دانلود',
                    activitySummary: 'خلاصه عملکرد',
                    lastMonthSells: 'فروش ماه گذشته',
                    totalSells: 'فروش کلی',
                    totalUpload: 'کل حجم آپلودی',
                    totalDownload: 'کل حجم دانلودی',
                    errorLoggingIn: 'مشکلی در ورود به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfLogin: 'فیلد هارا به صورت درست پر کنید',
                    errorSavingCustomer: 'مشکلی در ثبت مشتری به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfCustomer: 'فیلد هارا به صورت درست پر کنید',
                    customerSavedSuccessfully : 'مشتری با موفقیت ثبت شد',
                    errorSavingServer: 'مشکلی در ثبت سرور به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfServer: 'فیلد هارا به صورت درست پر کنید',
                    serverSavedSuccessfully : 'سرور با موفقیت ثبت شد',
                    inboundsLoadFailed : 'عملیات با خطا مواجه شد.دوباره امتحان کنید.',
                    inboundsLoadSuccessfully : 'دسته کانفیگ ها با موفقیت بروزرسانی شدند.',
                    subscriptionSavedSuccessfully : 'اشتراک /ها با موفقیت ثبت شد.',
                    errorSavingSubscription: 'مشکلی در ثبت اشتراک /ها به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfSubscription: 'فیلد هارا به صورت درست پر کنید.',
                    number : 'تعداد',
                    databaseUploadSuccessful : 'پایگاه داده با موفقیت آپلود شد.',
                    databaseUploadFailed : 'آپلود پایگاه داده با خطا مواجه شد دوباره امتحان کنید.',
                    syncWithPanel : 'همگام سازی اشتراک ها',
                    syncWithPanelSuccessful : 'اشتراک ها با پنل همگام شدند.',
                    syncWithPanelFailed : 'همگام‌سازی با پنل با خطا مواجه شد.دوباره سعی کنید.',
                    noRecords: 'رکوردی برای نمایش وجود ندارد',
                    syncSubs : 'همگام‌سازی اشتراک ها',
                    subsSyncedSuccessfully : 'اشتراک‌ها با موفقیت همگام شدند.',
                    syncSubsFailed : 'همگام‌سازی اشتراک‌ها با خطا مواجه شد.دوباره سعی کنید.',
                    days : 'روز'
                },
                en: {
                    username: 'Username',
                    password: 'Password',
                    firstName: 'First name',
                    lastName: 'Last name',
                    email: 'Email',
                    phoneNumber: 'Phone number',
                    address: 'Address',
                    signIn: 'Sign in',
                    signOut: 'Sign out',
                    dashboard: 'Dashboard',
                    servers: 'Servers',
                    customers: 'Customers',
                    customer: 'Customer',
                    subscriptions: 'Subscriptions',
                    subscription: 'Subscription',
                    new: 'new',
                    edit: 'Edit',
                    delete: 'Delete',
                    update: 'Update',
                    add: 'Add',
                    id: 'ID',
                    serverUrl: 'Server url',
                    server: 'Server',
                    information: 'Information',
                    generatable: 'Generatable',
                    actions: 'Actions',
                    active: 'Active',
                    activate: 'Activate',
                    deactivate: 'Deactivate',
                    inactive: 'Inactive',
                    inbounds: 'Inbounds',
                    inbound: 'Inbound',
                    title: 'title',
                    port: 'port',
                    status: 'status',
                    save: 'Save',
                    cancel: 'Cancel',
                    show: 'Show',
                    url: 'Url',
                    startEndDate: 'Start/End Date',
                    usage: 'Usage',
                    renew: 'renew',
                    totalFlow : 'Total flow',
                    ipLimit: 'Ip limit',
                    periodLength: 'Period length (days)',
                    clickToCopy: 'Click on QR code or link to copy link.',
                    subscriptionUrl: 'Subscription link',
                    copySuccessful : 'Link copied successfully',
                    copyUnsuccessful : 'Error occurred',
                    subLookUp: 'Subscription status lookup',
                    startDate: 'Start date',
                    expireDate: 'Expire date',
                    totalUsed: 'Total used',
                    enterSubToSearch : 'Enter subscription link and press magnifier to lookup subscription status.',
                    database: 'Database',
                    upload: 'Upload',
                    download: 'Download',
                    activitySummary: 'Activity summary',
                    lastMonthSells: 'Last month sells',
                    totalSells: 'Total sells',
                    totalUpload: 'Total upload',
                    totalDownload: 'Total download',
                    errorLoggingIn: 'Error occurred while logging in.Try again.',
                    errorFieldsOfLogin: 'Fill the field correctly.',
                    errorSavingCustomer: 'Error occurred while saving customer.Try again.',
                    errorFieldsOfCustomer: 'Fill the fields correctly.',
                    customerSavedSuccessfully : 'Customer saved successfully',
                    errorSavingServer: 'Error occurred while saving server.Try again.',
                    errorFieldsOfServer: 'Fill the fields correctly.',
                    serverSavedSuccessfully : 'Server saved successfully',
                    inboundsLoadFailed : 'Loading inbounds failed.Try again.',
                    inboundsLoadSuccessfully : 'Inbounds loaded successfully.',
                    subscriptionSavedSuccessfully : 'Subscription /s Saved Successfully.',
                    errorSavingSubscription: 'Error occurred while saving Subscription /s.Try again.',
                    errorFieldsOfSubscription: 'Fill the fields correctly.',
                    number : 'Number',
                    databaseUploadSuccessful : 'Database uploaded successfully',
                    databaseUploadFailed : 'Database upload failed.Try again.',
                    syncWithPanel : 'Sync subscriptions',
                    syncWithPanelSuccessful : 'Subscriptions synced with panel successfully.',
                    syncWithPanelFailed : 'Error occurred while syncing.Try again.',
                    noRecords : 'No records to show',
                    syncSubs : 'Sync subscriptions',
                    subsSyncedSuccessfully : 'Subscriptions synced successfully.',
                    syncSubsFailed : 'Error occurred while syncing.Try again.',
                    days : 'Days'
                },
            }
        }
    },
    getters: {
        getLanguage() {
            return this.language
        },
        getFlag() {
            return this.flag
        },
        getLocal() {
            return this.locals[this.language]
        },
        getDirection() {
            return this.direction
        },
    },
    actions: {
        changeLanguage(payload) {
            this.flag = payload[0]
            this.language = payload[1]
            this.direction = payload[2]
        },
    }
})