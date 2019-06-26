package com.spacesofting.weshare.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import com.spacesofting.weshare.mvp.RoleEnum
import com.spacesofting.weshare.ui.fragment.ProfileEditFragment
import com.spacesofting.weshare.ui.fragment.ui.fragment.*
import com.spacesofting.weshare.ui.ui.fragment.MyMapFragment

object ScreenPool {

    val MAIN_FRAGMENT = "main_fragment"
    val BASE_FRAGMENT = "base_fragment"
    val MAP_FRAGMENT = "map_fragment"
    val FEED_FRAGMENT = "feed_fragment"



    val INVENTORY_FRAGMENT = "inventory_fragment"
    val PROFILEEDIT = "profile_edit_fragment"
    val SPLASH_FRAGMENT = "splash_fragment"
    val REGISTRATION_FRAGMENT = "registration_fragment"
    val SMS_CONFIRMATION_FRAGMENT = "sms_confirmation_fragment"
    val TICKET_FRAGMENT = "ticket_fragment"
    val SECURITY_FRAGMENT = "security_fragment"
    val GUEST_CARD_FRAGMENT = "guest_card_fragment"
    val APPROVE_LIST_FRAGMENT = "approve_list_fragment"
    val SCANNER_ACTIVITY = "scanner_activity"
    val CALL_PHONE_ACTIVITY = "call_phone_activity"
    val EDIT_GUEST_CARD_FRAGMENT = "edit_guest_card_fragment"
    val COMPANIES_LIST_FARGMENT = "companies_list_fragment"
    val STRUCTURE_UNIT_LIST_FRAGMENT = "structure_unit_list_fragment"
    val SUO_FRAGMENT = "structure_unit_officer_fragment"
    val GUEST_RESPONSIBLE_FRAGMENT = "guest_responsible_fragment"
    val GUEST_MANAGER_FRAGMENT = "guest_manager_fragment"

    fun getFragment(screenKey: String, data: Any? = null): Fragment? {
        return when (screenKey) {

            SPLASH_FRAGMENT -> {
                SplashFragment.getInstance()
            }
            REGISTRATION_FRAGMENT -> {
                RegistrationPhoneFragment.getInstance()
            }
            BASE_FRAGMENT -> {
                BaseFragment.newInstance(data as String?)
            }
            INVENTORY_FRAGMENT -> {
                InventoryFragment.newInstance(data as Int?)
            }
            PROFILEEDIT -> {
                ProfileEditFragment.newInstance()
            }
            MAP_FRAGMENT -> {
               MyMapFragment.newInstance()
            }





            /*  REGISTRATION_FRAGMENT -> {
                  RegistrationPhoneFragment.getInstance()
              }
              SMS_CONFIRMATION_FRAGMENT -> {
                  SMSConfirmFragment.getInstance(data as? SmsRegistration)
              }
              TICKET_FRAGMENT -> {
                  TicketFragment.getInstance()
              }
              SECURITY_FRAGMENT -> {
                  SecurityFragment.getInstance()
              }*/

           /* COMPANIES_LIST_FARGMENT -> {
                CompaniesListFragment.getInstance()
            }
            STRUCTURE_UNIT_LIST_FRAGMENT -> {
                StructureUnitListFragment.getInstance()
            }
            SUO_FRAGMENT -> {
                SUOFragment.getInstance()
            }
            GUEST_MANAGER_FRAGMENT -> {
                EventManagerFragment.getInstance()
            }
            GUEST_RESPONSIBLE_FRAGMENT -> {
                GuestResponsibleFragment.getInstance()
            }*/
            else -> null
        }
    }

    fun getActivity(context: Context, screenKey: String, data: Any? = null): Intent? {
        return when (screenKey) {
           /* SCANNER_ACTIVITY -> {
               // ScannerActivity.getIntent(context)
            }*/
            CALL_PHONE_ACTIVITY -> {
                Intent(Intent.ACTION_CALL, Uri.parse("tel: ${data as String}"))
            }
            else -> null
        }
    }

    private fun getMainFragment(role: RoleEnum, data: Any? = null): Fragment? {
        return when (role) {
            RoleEnum.ADMINISTRATOR -> StubRolesFragment.getInstance()//not implemented
            RoleEnum.GUEST -> getFragment(TICKET_FRAGMENT)
            RoleEnum.GUEST_MANAGER -> StubRolesFragment.getInstance()//not implemented
            RoleEnum.EVENT_MANAGER -> getFragment(GUEST_MANAGER_FRAGMENT)
            RoleEnum.GUEST_RESPONSIBLE -> getFragment(GUEST_RESPONSIBLE_FRAGMENT)
            RoleEnum.STRUCTURE_UNIT_OFFICER -> getFragment(SUO_FRAGMENT)
            RoleEnum.SECURITY -> getFragment(SECURITY_FRAGMENT)
            RoleEnum.NONE -> StubRolesFragment.getInstance()//not implemented
        }
    }

    private fun getGuestCardFragment(role: RoleEnum, data: Any? = null): Fragment? {
        return when (role) {
            /*RoleEnum.SECURITY -> {
                GuestCardForSecurityFragment.getInstance(data as Bundle)
            }
            RoleEnum.STRUCTURE_UNIT_OFFICER -> {
                GuestCardForSUOFragment.getInstance(data as GuestCard)
            }*/
            RoleEnum.GUEST -> {
                TODO()//get guest card for Guest
            }
            RoleEnum.EVENT_MANAGER -> {
                TODO()//get guest card for Event manager
            }
            //etc....
            else -> null
        }
    }

    private fun getEditGuestCardFragment(role: RoleEnum, data: Any? = null): Fragment? {
        return when (role) {
            RoleEnum.SECURITY -> {
                TODO()//get guest card for Security
            }
          /*  RoleEnum.STRUCTURE_UNIT_OFFICER -> {
             //   EditGuestCardForSUOFragment.getInstance(data as GuestCard)
            }*/
            RoleEnum.GUEST -> {
                TODO()//get guest card for Guest
            }
            RoleEnum.EVENT_MANAGER -> {
                TODO()//get guest card for Event manager
            }
            //etc....
            else -> null
        }
    }
}