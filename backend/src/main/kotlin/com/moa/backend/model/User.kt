package com.moa.backend.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
open class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var firstName: String,

    var lastName: String,

    var email: String,

    private var password: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

    @ManyToMany(mappedBy = "likes")
    var likedIdeas: MutableList<Idea>?,

    @ManyToMany(mappedBy = "likes")
    var likedComments: MutableList<Comment>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var ideas: MutableList<Idea>?,

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var ideaBoxes: MutableList<IdeaBox>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment>?
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = ArrayList<SimpleGrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(role.name))
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}