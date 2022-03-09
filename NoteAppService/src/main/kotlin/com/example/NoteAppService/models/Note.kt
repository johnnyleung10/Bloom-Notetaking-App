package com.example.NoteAppService.models

import javax.persistence.*

@Entity
@Table(name = "NOTE_TABLE")
data class Note (
    @Id
    var id: Long,
    var title: String?,

    var contentRich: String?,

    var contentPlain: String?,

    var dateCreated: String?,

    var dateModified: String?,

    var dateDeleted: String?,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "folder_id", referencedColumnName="id")
    var folderId: Long?
    )