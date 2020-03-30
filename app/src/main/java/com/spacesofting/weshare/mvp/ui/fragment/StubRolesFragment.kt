package com.spacesofting.weshare.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.R

//TODO: remove this fragment after implement all roles
class StubRolesFragment : FragmentWrapper() {


    companion object {
        fun getInstance() =
            StubRolesFragment()
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_stub_roles
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setHomeAsUpIndicator(TOOLBAR_INDICATOR_HAMBURGER)
        showToolbar(TOOLBAR_HIDE)
        val notImpmented = "NOT IMPLEMENTED"

     /*   when(AccountManager.role) {
            RoleEnum.ADMINISTRATOR -> text.text = "Administrator\n$notImpmented"
            RoleEnum.GUEST_MANAGER -> text.text = "Guest manager\n$notImpmented"
            RoleEnum.EVENT_MANAGER -> text.text = "Event manager\n$notImpmented"
            RoleEnum.GUEST_RESPONSIBLE -> text.text = "Guest responsible\n$notImpmented"
            RoleEnum.STRUCTURE_UNIT_OFFICER -> text.text = "Structure unit officer\n$notImpmented"
        }*/
    }
}