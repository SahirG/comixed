/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, Darryl L. Pierce
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.package
 * org.comixed;
 */

package org.comixed.web.controllers;

import java.security.Principal;

import org.comixed.library.model.ComixEdUser;
import org.comixed.repositories.ComixEdUserRepository;
import org.comixed.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class UserController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComixEdUserRepository userRepository;
    @Autowired
    private Utils utils;

    @RequestMapping(value = "/user",
                    method = RequestMethod.GET)
    public Principal getCurrentUser(Principal user)
    {
        return user;
    }

    @RequestMapping(value = "/user/property",
                    method = RequestMethod.GET)
    public String getUserProperty(Authentication authentication, @RequestParam("name") String name)
    {
        this.logger.debug("Loading user: email={}", authentication.getName());
        ComixEdUser user = this.userRepository.findByEmail(authentication.getName());

        this.logger.debug("Return property: {}={}", name, user.getPreference(name));
        return user.getPreference(name);
    }

    @RequestMapping(value = "/user/property",
                    method = RequestMethod.POST)
    public void setUserProperty(Authentication authentication,
                                @RequestParam("name") String name,
                                @RequestParam("value") String value)
    {
        this.logger.debug("Loading user: email={}", authentication.getName());
        ComixEdUser user = this.userRepository.findByEmail(authentication.getName());

        this.logger.debug("Setting property: {}={}", name, value);
        user.setProperty(name, value);
        this.userRepository.save(user);
    }

    @RequestMapping(value = "/user/update",
                    method = RequestMethod.POST)
    public void update(Authentication authentication,
                       @RequestParam("username") String username,
                       @RequestParam("password") String password)
    {
        this.logger.debug("Updating user account for: {}", authentication.getName());
        ComixEdUser user = this.userRepository.findByEmail(authentication.getName());

        user.setEmail(username);
        user.setPasswordHash(this.utils.createHash(password.getBytes()));

        this.userRepository.save(user);
    }
}