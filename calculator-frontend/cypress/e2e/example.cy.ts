// https://on.cypress.io/api

describe('E2E tests on Forms', () => {
  it('disable submit button if invalid input', () => {
    cy.visit('/review')
    cy.get('input[type="text"]').type(' ')
    cy.get('input[type="email"]').type('invalid-email')
    cy.get('textarea').type('short')

    cy.get('button[type="submit"]').should('be.disabled')
  })

  it('enable submit button if valid input and check if submission is successful', () => {
    cy.visit('/review')
    cy.get('input[type="text"]').type('kaamya')
    cy.get('input[type="email"]').type('myemail@gmail.com')
    cy.get('textarea').type('I kinda like cypress ngl')

    cy.get('button[type="submit"]').should('be.enabled')

    cy.get('button[type="submit"]').click()

    cy.on('window:alert', (alertText) => {
      expect(alertText).to.equal('Success: Your review has been submitted!')
    })


  })
})
