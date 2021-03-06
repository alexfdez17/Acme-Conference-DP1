
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name")
})
public class Topic extends DomainEntity {

	//Attributes
	private String	name;
	private String	nameES;


	//Attributes

	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml
	public String getNameES() {
		return this.nameES;
	}

	public void setNameES(final String nameES) {
		this.nameES = nameES;
	}

}
