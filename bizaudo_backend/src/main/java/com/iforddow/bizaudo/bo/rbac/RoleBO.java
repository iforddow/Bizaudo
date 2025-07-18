package com.iforddow.bizaudo.bo.rbac;

import com.iforddow.bizaudo.util.BizUtils;

public class RoleBO {

    /**
    * A method that ensures the code name
    * will be valid. Designed to help with
    * working with roles programmatically.
    *
    * @author IFD
    * @since 2024-07-18
    * */
    String getValidCodeName(String name) {

        StringBuilder validName = new StringBuilder();

        name = name.replaceAll("[^A-Za-z]+", " ");

        String[] nameSections = name.split("[\\s\\-_]+");

        for(String section : nameSections){

            if(!section.isEmpty()) {

                section = BizUtils.capitalizeFirstLetter(section);

                validName.append(section);

            }
        }

        return validName.toString();

    }

}
