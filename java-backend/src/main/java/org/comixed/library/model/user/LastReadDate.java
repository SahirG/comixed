/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2018, The ComiXed Project
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

package org.comixed.library.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.comixed.library.model.ComiXedUser;
import org.comixed.library.model.Comic;
import org.comixed.library.model.View;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "user_last_read_dates")
@NamedQueries(
{@NamedQuery(name = "LastReadDate.findAllForUser",
             query = "SELECT d FROM LastReadDate d WHERE d.user.id = :userId")})
public class LastReadDate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.UserDetails.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comic_id")
    private Comic comic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ComiXedUser user;

    @Column(name = "last_read",
            nullable = false,
            updatable = false)
    @JsonProperty("last_read")
    @JsonView(View.UserDetails.class)
    private Date lastRead = new Date();

    public Comic getComic()
    {
        return this.comic;
    }

    public Long getId()
    {
        return this.id;
    }

    public Date getLastRead()
    {
        return this.lastRead;
    }

    public ComiXedUser getUser()
    {
        return this.user;
    }

    public void setLastRead(Date lastRead)
    {
        this.lastRead = lastRead;
    }
}
