package com.moa.backend.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
open class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,

    open var firstName: String,

    open var lastName: String,

    open var email: String,

    private var password: String,

    @Enumerated(EnumType.STRING)
    open var role: Role,

    @ManyToMany(mappedBy = "likes")
    open var likedIdeas: MutableList<Idea>?,

    @ManyToMany(mappedBy = "likes")
    open var likedComments: MutableList<Comment>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var ideas: MutableList<Idea>?,

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var ideaBoxes: MutableList<IdeaBox>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var comments: MutableList<Comment>?
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