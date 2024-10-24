package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "user")
open class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "firstName")
    open var firstName: String,

    @Column(name = "lastName")
    open var lastName: String,

    @Column(name = "email")
    open var email: String,

    @Column(name = "password")
    private var password: String,

    @Column(name = "role")
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

    @ManyToMany(mappedBy = "requiredJuries")
    open var ideasToJury: MutableList<Idea>?,

    @ManyToMany(mappedBy = "defaultRequiredJuries")
    open var requiredToJury: MutableList<IdeaBox>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var scoreSheets: MutableList<ScoreSheet>?,

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var comments: MutableList<Comment>?
) : UserDetails {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        return id == other.id &&
                firstName == other.firstName &&
                lastName == other.lastName &&
                email == other.email &&
                role == other.role &&
                password == other.password
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}
