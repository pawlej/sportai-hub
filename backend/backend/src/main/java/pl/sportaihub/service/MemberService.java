package pl.sportaihub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sportaihub.aspect.LogActivity;
import pl.sportaihub.dto.MemberRequest;
import pl.sportaihub.dto.MemberResponse;
import pl.sportaihub.entity.Member;
import pl.sportaihub.enums.ActivityType;
import pl.sportaihub.exception.BadRequestException;
import pl.sportaihub.exception.NotFoundException;
import pl.sportaihub.repository.MemberRepository;
import pl.sportaihub.repository.ProjectRepository;
import pl.sportaihub.repository.ProjectTaskRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository taskRepository;

    public MemberService(
            MemberRepository memberRepository,
            ProjectRepository projectRepository,
            ProjectTaskRepository taskRepository
    ) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        return toResponse(getMember(id));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.MEMBER_CREATED,
            message = "Dodano członka koła"
    )
    public MemberResponse create(MemberRequest request) {
        if (memberRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("Member with this email already exists");
        }

        Member member = new Member();
        updateEntity(member, request);

        return toResponse(memberRepository.save(member));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.MEMBER_UPDATED,
            message = "Zaktualizowano członka koła"
    )
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = getMember(id);

        if (memberRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new BadRequestException("Another member uses this email");
        }

        updateEntity(member, request);

        return toResponse(memberRepository.save(member));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.MEMBER_DELETED,
            message = "Usunięto członka koła"
    )
    public void delete(Long id) {
        Member member = getMember(id);

        if (projectRepository.existsByLeaderId(id)) {
            throw new BadRequestException(
                    "Cannot delete member because this member leads a project"
            );
        }

        if (taskRepository.existsByAssignedMemberId(id)) {
            throw new BadRequestException(
                    "Cannot delete member because tasks are assigned to this member"
            );
        }

        memberRepository.delete(member);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Member not found: " + id)
                );
    }

    private void updateEntity(Member member, MemberRequest request) {
        member.setFirstName(request.firstName().trim());
        member.setLastName(request.lastName().trim());
        member.setEmail(request.email().trim().toLowerCase());
        member.setSpecialization(request.specialization());
        member.setActive(request.active());
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getSpecialization(),
                member.isActive(),
                member.getCreatedAt()
        );
    }
}