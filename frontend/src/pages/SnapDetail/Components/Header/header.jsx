import React, { useState } from 'react'

import Logo from "shared/ui/Logo/Logo"
import {BackButton} from "widgets/Button/Button"
import MenuIcons from "pages/SnapDetail/Components/MenuIcons/MenuIcons.jsx";

const Header = () => {

    return (
        <header className='snap-detail__header' >
            <BackButton className={"snap-detail__back-button"}/>
            <Logo title={"#SNAP"} />
            <MenuIcons/>
        </header>
    )
}

export default Header